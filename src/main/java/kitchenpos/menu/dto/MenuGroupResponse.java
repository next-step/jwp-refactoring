package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {

	private Long id;
	private String name;

	private MenuGroupResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MenuGroupResponse from(MenuGroup menuGroup) {
		return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName().value());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
