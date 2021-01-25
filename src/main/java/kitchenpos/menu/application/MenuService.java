package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuProductRepository menuProductRepository;

	public MenuService(
		final MenuRepository menuRepository,
		final MenuProductRepository menuProductRepository
	) {
		this.menuRepository = menuRepository;
		this.menuProductRepository = menuProductRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {
		Menu saveTargetMenu = new Menu.Builder()
			.name(menuRequest.getName())
			.price(menuRequest.getPrice())
			.menuGroupId(menuRequest.getMenuGroupId())
			.build();

		Menu savedMenu = menuRepository.save(saveTargetMenu);
		Menu finalSavedMenu = addCreateMenuProductTarget(savedMenu, menuRequest.getMenuGroupId());
		return MenuResponse.of(finalSavedMenu);
	}

	public List<MenuResponse> list() {
		final List<Menu> menus = menuRepository.findAll();
		return menus.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
	}

	private Menu addCreateMenuProductTarget(Menu savedMenu, Long menuGroupId) {
		final List<MenuProduct> findMenuProducts = menuProductRepository.findAllByMenuId(menuGroupId);
		MenuProducts menuProducts = new MenuProducts(findMenuProducts);
		savedMenu.addMenuProducts(menuProducts);
		return savedMenu;
	}

}
