package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
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

	public MenuService(final MenuRepository menuRepository, final MenuGroupService menuGroupService,
		ProductService productService) {
		this.menuRepository = menuRepository;
		this.menuGroupService = menuGroupService;
		this.productService = productService;
	}

	public MenuResponse create(final MenuRequest request) {
		MenuGroup menuGroup = menuGroupService.getById(request.getMenuGroupId());
		Menu menu = request.toEntity(menuGroup);
		final Menu persistMenu = menuRepository.save(menu);
		List<MenuProduct> menuProducts = request.getMenuProducts().stream()
			.map(menuProductRequest -> {
				Product product = productService.getById(menuProductRequest.getProductId());
				return menuProductRequest.toEntity(persistMenu, product);
			}).collect(Collectors.toList());
		menu.addMenuProducts(menuProducts);
		return new MenuResponse(menu);
	}

	@Transactional(readOnly = true)
	public List<MenuResponse> list() {
		List<Menu> menuList = menuRepository.findAll();
		return menuList.stream().map(MenuResponse::new).collect(Collectors.toList());
	}
}
