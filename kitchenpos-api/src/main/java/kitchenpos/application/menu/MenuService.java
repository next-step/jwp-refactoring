package kitchenpos.application.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final MenuProductService menuProductService;

	public MenuService(
			final MenuRepository menuRepository,
			final MenuGroupRepository menuGroupRepository,
			final MenuProductRepository menuProductRepository,
			final MenuProductService menuProductService
	) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.menuProductService = menuProductService;
	}

	@Transactional
	public MenuResponse createMenu(final MenuRequest request) {
		MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId()).orElseThrow(() -> new IllegalArgumentException());
		final Menu menu = menuRepository.save(Menu.of(request.getName(), request.getPrice(), menuGroup));
		request.getMenuProductRequests().forEach(menuProductRequest ->
				menu.add(menuProductService.saveProduct(menu.getId(), menuProductRequest)));
		menu.validatePrice();
		return MenuResponse.of(menu);
	}

	public List<MenuResponse> listMenus() {
		List<Menu> savedMenus = menuRepository.findAll();
		return savedMenus.stream()
				.map(menu -> {
					List<MenuProduct> menuProducts = menuProductService.findAllByMenuId(menu.getId());
					return MenuResponse.of(menuProducts, menu);
				})
				.collect(Collectors.toList());
	}
}
