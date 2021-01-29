package kitchenpos.menugroup.dto;

import kitchenpos.domain.MenuGroup;

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
