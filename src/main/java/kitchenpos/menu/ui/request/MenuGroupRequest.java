package kitchenpos.menu.ui.request;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {

	private final String name;

	public MenuGroupRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public MenuGroup toEntity() {
		return MenuGroup.from(name);
	}
}
