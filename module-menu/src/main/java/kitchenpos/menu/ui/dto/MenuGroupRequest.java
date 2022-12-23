package kitchenpos.menu.ui.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {

	private String name;

	private MenuGroupRequest() {
	}

	public MenuGroupRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public MenuGroup toMenuGroup() {
		return new MenuGroup(name);
	}
}
