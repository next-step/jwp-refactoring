package kitchenpos.acceptance.menugroup;

import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;

public class MenuGroupFixture {

	public static MenuGroupRequest 메뉴그룹() {
		return new MenuGroupRequest("메뉴그룹1");
	}

	public static MenuGroupResponse 메뉴그룹(long id, String name) {
		return new MenuGroupResponse(id, name);
	}
}
