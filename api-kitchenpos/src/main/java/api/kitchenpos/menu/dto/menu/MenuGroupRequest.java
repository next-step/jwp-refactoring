package api.kitchenpos.menu.dto.menu;

import domain.kitchenpos.menu.menu.MenuGroup;

public class MenuGroupRequest {

	private String name;

	public MenuGroupRequest(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public MenuGroup toMenuGroup() {
		return new MenuGroup(this.name);
	}
}
