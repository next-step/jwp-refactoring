package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {
	private String name;

	public MenuGroupRequest() {
	}

	public MenuGroupRequest(String name) {
		this.name = name;
	}

	public static MenuGroupRequest of(String name) {
		return new MenuGroupRequest(name);
	}

	public MenuGroup toMenuGroup() {
		return MenuGroup.of(name);
	}

	public String getName() {
		return this.name;
	}
}
