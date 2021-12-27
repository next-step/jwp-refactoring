package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuValidator menuValidator;

	public MenuService(
		final MenuRepository menuRepository,
		final MenuValidator menuValidator
	) {
		this.menuRepository = menuRepository;
		this.menuValidator = menuValidator;
	}

	@Transactional
	public Menu create(final MenuRequest menuRequest) {
		menuValidator.validate(menuRequest);
		final Menu savedMenu = Menu.from(menuRequest);
		savedMenu.addMenuProducts(generateMenuProducts(menuRequest, savedMenu));
		return menuRepository.save(savedMenu);
	}

	public List<Menu> list() {
		return menuRepository.findAll();
	}

	private List<MenuProduct> generateMenuProducts(MenuRequest menuRequest, Menu menu) {
		return menuRequest.getMenuProducts()
			.stream()
			.map(it -> generateMenuProduct(menu, it))
			.collect(Collectors.toList());
	}

	private MenuProduct generateMenuProduct(Menu menu, MenuProductRequest menuProductRequest) {
		return MenuProduct.of(menu, menuProductRequest.getProductId(), menuProductRequest.getQuantity());
	}
}
