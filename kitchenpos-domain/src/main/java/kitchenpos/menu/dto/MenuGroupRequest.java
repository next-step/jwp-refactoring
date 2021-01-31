package kitchenpos.menu.dto;

import kitchenpos.menu.MenuGroup;

public class MenuGroupRequest {
	private String name;

	protected MenuGroupRequest() {
	}

	public MenuGroupRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public MenuGroup toMenuGroup(){
		return MenuGroup.builder().name(name).build();
	}
}
