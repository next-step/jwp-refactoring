package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {

	public String name;

	protected MenuGroupRequest() {
	}

	private MenuGroupRequest(String name) {
		this.name = name;
	}

	public static MenuGroupRequest from(String name) {
		return new MenuGroupRequest(name);
	}

	public String getName() {
		return name;
	}

	public MenuGroup toEntity() {
		return MenuGroup.from(name);
	}
}
