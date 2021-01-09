package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;
import kitchenpos.exception.NotFoundException;

public class MenuGroupResponse {

	private Long id;
	private String name;

	private MenuGroupResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MenuGroupResponse of(MenuGroup menuGroup) {
		if (menuGroup == null) {
			throw new NotFoundException("메뉴 그룹 정보를 찾을 수 없습니다.");
		}
		return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
