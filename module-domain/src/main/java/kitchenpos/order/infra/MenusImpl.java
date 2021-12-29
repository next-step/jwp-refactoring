package kitchenpos.order.infra;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Menus;

@Component
public class MenusImpl implements Menus {
	private final MenuRepository menuRepository;

	public MenusImpl(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}

	@Override
	public boolean containsAll(List<Long> ids) {
		List<Menu> menus = menuRepository.findAllByIdIn(ids);
		return menus.size() == ids.size();
	}
}
