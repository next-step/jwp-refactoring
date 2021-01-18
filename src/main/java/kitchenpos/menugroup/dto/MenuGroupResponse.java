package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupResponse {
	private Long id;

	private String name;

	public MenuGroupResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List<MenuGroupResponse> of(List<MenuGroup> menuGroup) {
		return menuGroup.stream()
				.map(MenuGroupResponse::of)
				.collect(Collectors.toList());
	}

	public static MenuGroupResponse of(MenuGroup menuGroup) {
		return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
	}
}
