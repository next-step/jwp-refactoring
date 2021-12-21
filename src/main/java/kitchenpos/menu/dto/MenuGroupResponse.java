package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {

	private Long id;
	private String name;

	protected MenuGroupResponse() {
	}

	private MenuGroupResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MenuGroupResponse of(Long id, String name) {
		return new MenuGroupResponse(id, name);
	}

	public static List<MenuGroupResponse> ofList(List<MenuGroup> menuGroupList) {
		return menuGroupList.stream()
			.map(MenuGroup::toResDto)
			.collect(Collectors.toList());
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}
}
