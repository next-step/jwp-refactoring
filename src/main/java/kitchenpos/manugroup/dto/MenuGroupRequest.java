package kitchenpos.manugroup.dto;

import kitchenpos.manugroup.domain.MenuGroup;

public class MenuGroupRequest {

	private String name;

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
