package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Name;

public class MenuGroupRequest {
	private String name;

	public MenuGroupRequest() {}

	public MenuGroupRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public MenuGroup toEntity() {
		return new MenuGroup(Name.valueOf(name));
	}
}
