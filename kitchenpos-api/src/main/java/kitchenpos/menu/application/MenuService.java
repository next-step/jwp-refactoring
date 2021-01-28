package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Products;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuProductRepository menuProductRepository;
	private final MenuGroupService menuGroupService;
	private final ProductService productService;

	public MenuService(
		final MenuRepository menuRepository,
		final MenuProductRepository menuProductRepository,
		final MenuGroupService menuGroupService,
		final ProductService productService
	) {
		this.menuRepository = menuRepository;
		this.menuProductRepository = menuProductRepository;
		this.menuGroupService = menuGroupService;
		this.productService = productService;
	}

	@Transactional
	public MenuResponse create(final MenuRequest request) {
		final MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());
		final Products products = productService.findAllByIds(request.productIds());
		final MenuProducts menuProducts = request.toMenuProducts(products, menuGroup);
		
		return MenuResponse.of(menuProductRepository.saveAll(menuProducts.getMenuProducts()));
	}

	public List<MenuResponse> list() {
		final List<Menu> menus = menuRepository.findAll();
		return menus.stream()
			.map(menu -> MenuResponse.of(menuProductRepository.findByMenuId(menu.getId())))
			.collect(Collectors.toList());
	}

	public List<Menu> findAllByIds(List<Long> menuIds) {
		return menuRepository.findAllById(menuIds);
	}
}
