package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;

@Service
@Transactional
public class MenuService {

	private final MenuRepository menuRepository;
	private final MenuGroupService menuGroupService;
	private final ProductService productService;

	public MenuService(final MenuRepository menuRepository,
		final MenuGroupService menuGroupService,
		final ProductService productService) {

		this.menuRepository = menuRepository;
		this.menuGroupService = menuGroupService;
		this.productService = productService;
	}

	public MenuResponse create(final MenuRequest request) {
		MenuGroup menuGroup = menuGroupService.getById(request.getMenuGroupId());
		Menu menu = request.toEntity(menuGroup);
		createMenuProducts(request.getMenuProducts(), menu);
		menu.checkOverPrice();
		return new MenuResponse(menuRepository.save(menu));
	}

	private void createMenuProducts(List<MenuProductRequest> menuProductRequests, Menu menu) {
		menuProductRequests.forEach(request -> createMenuProduct(request, menu));
	}

	private void createMenuProduct(MenuProductRequest request, Menu menu) {
		Product product = productService.getById(request.getProductId());
		MenuProduct.create(menu, product, request.getQuantity());
	}

	@Transactional(readOnly = true)
	public List<MenuResponse> list() {
		List<Menu> menuList = menuRepository.findAll();
		return menuList.stream().map(MenuResponse::new).collect(Collectors.toList());
	}
}
