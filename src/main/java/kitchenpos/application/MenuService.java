package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

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
		Menu menu = new Menu(menuRequest.getName(), new Price(menuRequest.getPrice()), menuGroup);

		List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
		for (MenuProductRequest menuProductRequest : menuProductRequests) {
			MenuProduct menuProduct = createMenuProduct(menuProductRequest);
			menu.addMenuProduct(menuProduct);
		}
		menu.validateMenuPrice();

		return MenuResponse.of(menuRepository.save(menu));
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
