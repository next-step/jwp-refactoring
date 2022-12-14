package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;

public class MenuGroupFixture {

	@Deprecated
	public static MenuGroup 메뉴그룹() {
		return new MenuGroup(1L, "메뉴그룹1");
	}

	public static MenuGroupRequest 메뉴그룹2() {
		return new MenuGroupRequest("메뉴그룹1");
	}

	public static MenuGroupResponse 메뉴그룹2(long id, String name) {
		return new MenuGroupResponse(id, name);
	}
}
