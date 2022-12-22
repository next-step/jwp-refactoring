package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuService(
		MenuRepository menuRepository,
		MenuGroupRepository menuGroupRepository,
		ProductRepository productRepository
	) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest request) {
		Menu menu = menuRepository.save(newMenu(request));
		return MenuResponse.from(menu);
	}

	@Transactional(readOnly = true)
	public List<MenuResponse> list() {
		return MenuResponse.listFrom(menuRepository.findAll());
	}


	private Menu newMenu(MenuRequest request) {
		return Menu.of(
			request.getName(),
			Price.from(request.getPrice()),
			menuGroupRepository.menuGroup(request.getMenuGroupId()),
			menuProducts(request.getMenuProducts())
		);
	}

	private MenuProducts menuProducts(List<MenuProductRequest> menuProductRequests) {
		return menuProductRequests.stream()
			.map(this::menuProduct)
			.collect(Collectors.collectingAndThen(Collectors.toList(), MenuProducts::from));
	}

	private MenuProduct menuProduct(MenuProductRequest menuProductRequest) {
		return MenuProduct.of(
			productRepository.product(menuProductRequest.getProductId()),
			Quantity.from(menuProductRequest.getQuantity())
		);
	}
}
