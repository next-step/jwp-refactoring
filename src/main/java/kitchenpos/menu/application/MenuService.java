package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.product.application.ProductService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuValidator menuValidator;

	public MenuService(MenuRepository menuRepository, MenuValidator menuValidator) {
		this.menuRepository = menuRepository;
		this.menuValidator = menuValidator;
	}

	@Transactional
	public Menu create(final MenuRequest menuRequest) {
		Menu menu = menuRequest.toMenu();
		menuValidator.validateCreate(menu);
		return menuRepository.save(menu);
	}

	@Transactional(readOnly = true)
	public List<Menu> list() {
		return menuRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Menu findById(long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다"));

	}
}
