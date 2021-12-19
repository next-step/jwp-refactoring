package kitchenpos.menugroup.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {
	private String name;

	public MenuGroupCreateRequest() {
	}

	public MenuGroupCreateRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MenuGroup toMenuGroup() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName(name);
		return menuGroup;
	}
}
