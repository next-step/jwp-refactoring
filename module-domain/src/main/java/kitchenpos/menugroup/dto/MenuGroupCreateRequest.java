package kitchenpos.menugroup.dto;

import kitchenpos.common.domain.Name;
import kitchenpos.menugroup.domain.MenuGroup;

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
		Name name = Name.from(this.name);
		return MenuGroup.from(name);
	}
}
