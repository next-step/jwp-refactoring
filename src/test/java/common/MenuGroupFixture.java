package common;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {


    public static MenuGroup 메뉴그룹_두마리() {

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("두마리메뉴");
        return menuGroup;
    }

    public static MenuGroup 메뉴그룹_한마리() {

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(2L);
        menuGroup.setName("한마리메뉴");

        return menuGroup;
    }

    public static MenuGroup 메뉴그룹_신메뉴() {

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(3L);
        menuGroup.setName("신메뉴");

        return menuGroup;
    }

}
