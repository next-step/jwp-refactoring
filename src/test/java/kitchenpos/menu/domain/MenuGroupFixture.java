package kitchenpos.menu.domain;

import kitchenpos.common.Name;

public class MenuGroupFixture {

    public static final String MENU_GROUP_A = "메뉴그룹 A";

    public static MenuGroup menuGroup() {
        return new MenuGroup(1L, new Name(MENU_GROUP_A));
    }
}
