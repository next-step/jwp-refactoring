package ktichenpos.menu.menu.ui.response;

import java.util.List;
import java.util.stream.Collectors;

import ktichenpos.menu.menu.domain.MenuGroup;

public class MenuGroupResponse {
	private Long id;
	private String name;

	private MenuGroupResponse() {
	}

	private MenuGroupResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MenuGroupResponse of(Long id, String name) {
		return new MenuGroupResponse(id, name);
	}

	public static MenuGroupResponse from(MenuGroup save) {
		return new MenuGroupResponse(save.id(), save.name());
	}

	public static List<MenuGroupResponse> listFrom(List<MenuGroup> menuGroups) {
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
