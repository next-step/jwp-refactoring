package common;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {


    public static MenuGroup 메뉴그룹_두마리() {
        return new MenuGroup(1L, "두마리메뉴");
    }

    public static MenuGroup 메뉴그룹_한마리() {
        return new MenuGroup(2L, "한마리메뉴");
    }

    public static MenuGroup 메뉴그룹_신메뉴() {
        return new MenuGroup(3L, "신메뉴");
    }

}
