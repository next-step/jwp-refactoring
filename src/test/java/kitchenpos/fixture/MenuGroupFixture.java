package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroup2;

public class MenuGroupFixture {

	@Deprecated
	public static MenuGroup 메뉴그룹() {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setId(1L);
		menuGroup.setName("베이커리");
		return menuGroup;
	}

	public static MenuGroup2 메뉴그룹2() {
		return new MenuGroup2(1L, "메뉴그룹1");
	}
}
