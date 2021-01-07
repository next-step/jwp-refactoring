package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductItem;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.NotFoundException;
import kitchenpos.exception.WrongPriceException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;
	private final MenuProductRepository menuProductRepository;

	public MenuService(
		final MenuRepository menuRepository,
		final MenuGroupRepository menuGroupRepository,
		final ProductRepository productRepository,
		final MenuProductRepository menuProductRepository
	) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
		this.menuProductRepository = menuProductRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {

		final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
			.orElseThrow(() -> new NotFoundException("메뉴 그룹 정보를 찾을 수 없습니다."));

		final Menu savedMenu = menuRepository.save(Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup));

		addMenuProduct(menuRequest, savedMenu);

		return MenuResponse.of(savedMenu);
	}

	private void addMenuProduct(MenuRequest menuRequest, Menu savedMenu) {
		BigDecimal sum = BigDecimal.ZERO;
		List<MenuProductItem> menuProductItems = menuRequest.getMenuProducts();
		for (MenuProductItem menuProductItem : menuProductItems) {
			Product product = productRepository.findById(menuProductItem.getProductId())
				.orElseThrow(() -> new NotFoundException("상품 정보를 찾을 수 없습니다."));
			MenuProduct menuProduct = MenuProduct.create(savedMenu, product, menuProductItem.getQuantity());
			savedMenu.addMenuProduct(menuProductRepository.save(menuProduct));
			sum = sum.add(menuProduct.getPrice());
		}
		validatePriceSum(menuRequest.getPrice(), sum);
	}

	private void validatePriceSum(BigDecimal price, BigDecimal sum) {
		if (price.compareTo(sum) > 0) {
			throw new WrongPriceException("메뉴의 가격이 상품가격의 총합보다 클 수 없습니다.");
		}
	}

	public List<MenuResponse> findAll() {
		final List<Menu> menus = menuRepository.findAllFetch();
		return menus.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
	}
}
