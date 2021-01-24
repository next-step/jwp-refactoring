package kitchenpos.menu.domain;

import java.util.List;

import javax.persistence.EntityNotFoundException;

public class Menus {
	private final List<Menu> menus;

	private Menus(final List<Menu> menus) {
		this.menus = menus;
	}

	public static Menus of(final List<Menu> menus) {
		return new Menus(menus);
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public Menu getMenu(Long menuId) {
		return this.menus.stream()
			.filter(menu -> menu.equalsId(menuId))
			.findFirst()
			.orElseThrow(EntityNotFoundException::new);
	}
}


