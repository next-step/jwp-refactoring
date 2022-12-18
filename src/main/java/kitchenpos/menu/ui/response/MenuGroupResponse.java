package kitchenpos.menu.ui.response;

import java.util.List;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {
	private final Long id;
	private final String name;

	private MenuGroupResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MenuGroupResponse of(Long id, String name) {
		return new MenuGroupResponse(id, name);
	}

	public static MenuGroupResponse from(MenuGroup save) {
		return new MenuGroupResponse(save.getId(), save.getName());
	}

	public static List<MenuGroupResponse> listFrom(List<MenuGroup> menuGroups) {
		return menuGroups.stream()
			.map(MenuGroupResponse::from)
			.collect(java.util.stream.Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
