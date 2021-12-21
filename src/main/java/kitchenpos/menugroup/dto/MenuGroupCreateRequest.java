package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupName;

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
		MenuGroupName name = MenuGroupName.of(this.name);
		return MenuGroup.of(name);
	}
}
