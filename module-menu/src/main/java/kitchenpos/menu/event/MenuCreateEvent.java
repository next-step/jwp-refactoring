package kitchenpos.menu.event;

import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuProductRequest;

public class MenuCreateEvent {

	private Menu menu;
	private List<MenuProductRequest> menuProductRequests;

	public MenuCreateEvent(Menu menu, List<MenuProductRequest> menuProductRequests) {
		this.menu = menu;
		this.menuProductRequests = menuProductRequests;
	}

	public Menu getMenu() {
		return menu;
	}

	public List<MenuProductRequest> getMenuProductRequests() {
		return menuProductRequests;
	}
}
