package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuValidator menuValidator;
	private final MenuProductRepository menuProductRepository;

	public MenuService(MenuRepository menuRepository, MenuValidator menuValidator,
		MenuProductRepository menuProductRepository) {
		this.menuRepository = menuRepository;
		this.menuValidator = menuValidator;
		this.menuProductRepository = menuProductRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {
		Menu menu = menuRequest.toMenu();
		MenuProducts menuProducts = menuRequest.toMenuProducts();
		menuValidator.validateCreate(menu, menuProducts);
		Menu savedMenu = menuRepository.save(menu);
		List<MenuProduct> savedMenuProducts = menuProductRepository.saveAll(menuProducts.setMenu(savedMenu).value());
		return MenuResponse.of(savedMenu, savedMenuProducts);
	}

	@Transactional(readOnly = true)
	public List<MenuResponse> list() {
		List<Menu> menus = menuRepository.findAll();
		return menus.stream()
			.map(menu -> MenuResponse.of(menu, findMenuProductsByMenuId(menu.getId())))
			.collect(Collectors.toList());
	}

	private List<MenuProduct> findMenuProductsByMenuId(Long menuId) {
		return menuProductRepository.findAllByMenuId(menuId);
	}

	@Transactional(readOnly = true)
	public Menu findById(long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다"));

	}
}
