package kitchenpos.menu.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;

@Service
@Transactional(readOnly = true)
public class MenuService {
	private final MenuRepository menuRepository;

	public MenuService(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}

	@Transactional
	public Menu create(Menu menu) {
		menu.validatePrice();
		return menuRepository.save(menu);
	}

	public List<Menu> findAll() {
		return menuRepository.findAll();
	}

	public List<Menu> findAllById(List<Long> idList) {
		return menuRepository.findAllById(idList);
	}
}
