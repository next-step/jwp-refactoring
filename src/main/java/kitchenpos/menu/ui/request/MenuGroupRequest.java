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
		// todo : id 값 없이 수정
		return new MenuGroup(null, name);
	}
}
