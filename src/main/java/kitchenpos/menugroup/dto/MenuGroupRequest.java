package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {
	private String name;

	public MenuGroupRequest() {
	}

	public MenuGroupRequest(final String name) {
		this.name = name;
	}

	public static MenuGroupRequest of(final String name) {
		return new MenuGroupRequest(name);
	}

	public MenuGroup toEntity() {
		return MenuGroup.of(name);
	}

	public String getName() {
		return this.name;
	}
}
