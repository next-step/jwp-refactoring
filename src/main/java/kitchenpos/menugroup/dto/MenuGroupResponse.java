package kitchenpos.menugroup.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

	private Long id;

	private String name;

	public MenuGroupResponse(final Long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public static MenuGroupResponse of(final MenuGroup menuGroup) {
		return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
	}

	public static List<MenuGroupResponse> ofList(final List<MenuGroup> menuGroups) {
		return menuGroups.stream()
			.map(MenuGroupResponse::of)
			.collect(Collectors.toList());
	}
}
