package ktichenpos.menu.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import ktichenpos.menu.menu.domain.Menu;
import ktichenpos.menu.menu.domain.MenuGroupRepository;
import ktichenpos.menu.menu.domain.MenuProduct;
import ktichenpos.menu.menu.domain.MenuProducts;
import ktichenpos.menu.menu.domain.MenuRepository;
import ktichenpos.menu.menu.domain.ProductRepository;
import ktichenpos.menu.menu.ui.request.MenuProductRequest;
import ktichenpos.menu.menu.ui.request.MenuRequest;
import ktichenpos.menu.menu.ui.response.MenuResponse;

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
