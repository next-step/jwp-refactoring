package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

	private Long id;
	private String name;

	private MenuGroupResponse() {
	}

	public MenuGroupResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public MenuGroupResponse(MenuGroup menuGroup) {
		this(menuGroup.getId(), menuGroup.getName());
	}

	public static List<MenuGroupResponse> of(List<MenuGroup> menuGroups) {
		return menuGroups.stream().map(MenuGroupResponse::new).collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
