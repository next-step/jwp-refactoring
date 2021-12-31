package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.domain.MenuGroup;

public class MenuGroupAddRequest {

	private String name;

	protected MenuGroupAddRequest() {
	}

	private MenuGroupAddRequest(String name) {
		this.name = name;
	}

	public static MenuGroupAddRequest of(String name) {
		return new MenuGroupAddRequest(name);
	}

	public String getName() {
		return name;
	}

	public MenuGroup toEntity() {
		return MenuGroup.of(name);
	}
}
