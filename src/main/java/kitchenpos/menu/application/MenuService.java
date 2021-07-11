package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
		ProductRepository productRepository) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {
		MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());
		List<MenuProduct> menuProducts = createMenuProducts(menuRequest);
		Menu menu = new Menu(menuRequest.getName(), new Price(menuRequest.getPrice()), menuGroup, menuProducts);
		return MenuResponse.of(menuRepository.save(menu));
	}

	private List<MenuProduct> createMenuProducts(MenuRequest menuRequest) {
		return menuRequest.getMenuProducts()
			.stream()
			.map(this::createMenuProduct)
			.collect(Collectors.toList());
	}

	private MenuGroup findMenuGroup(Long menuGroupId) {
		return menuGroupRepository.findById(menuGroupId)
			.orElseThrow(() -> new IllegalArgumentException("id에 해당하는 메뉴 그룹을 찾을 수 없습니다."));
	}

	public List<MenuResponse> list() {
		return menuRepository.findAll().stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
	}

	private MenuProduct createMenuProduct(MenuProductRequest menuProductRequest) {
		Product product = findProduct(menuProductRequest.getProductId());
		return new MenuProduct(product, menuProductRequest.getQuantity());
	}

	private Product findProduct(Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("id에 해당하는 상품을 찾을 수 없습니다"));
	}
}
