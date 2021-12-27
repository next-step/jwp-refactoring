package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {

	private Long id;
	private String name;

	public MenuGroupResponse() {
	}

	public MenuGroupResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public MenuGroupResponse(MenuGroup menuGroup) {
		this.id = menuGroup.getId();
		this.name = menuGroup.getName().toText();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
