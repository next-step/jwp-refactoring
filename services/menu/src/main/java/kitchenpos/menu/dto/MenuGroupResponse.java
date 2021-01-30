package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

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

	public static MenuGroupResponse from(MenuGroup menuGroup) {
		if(menuGroup == null) {
			return null;
		}
		return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
	}

	public static List<MenuGroupResponse> newList(List<MenuGroup> menuGroups) {
		return menuGroups.stream()
			.map(MenuGroupResponse::from)
			.collect(Collectors.toList());
	}


	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
