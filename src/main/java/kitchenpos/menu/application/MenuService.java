package kitchenpos.menu.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;

@Service
@Transactional(readOnly = true)
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuValidator menuValidator;

	public MenuService(MenuRepository menuRepository,
		MenuValidator menuValidator) {
		this.menuRepository = menuRepository;
		this.menuValidator = menuValidator;
	}

	@Transactional
	public Menu create(Menu menu) {
		menuValidator.validate(menu);
		return menuRepository.save(menu);
	}

	public List<Menu> findAll() {
		return menuRepository.findAll();
	}

	public List<Menu> findAllById(List<Long> idList) {
		return menuRepository.findAllById(idList);
	}
}
