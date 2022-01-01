package kitchenpos.moduledomain.common;

import kitchenpos.moduledomain.menu.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴그룹_두마리() {
        return MenuGroup.of(1L, "두마리메뉴");
    }

    public static MenuGroup 메뉴그룹_한마리() {
        return MenuGroup.of(2L, "한마리메뉴");
    }

    public static MenuGroup 메뉴그룹_신메뉴() {
        return MenuGroup.of(3L, "신메뉴");
    }

}
