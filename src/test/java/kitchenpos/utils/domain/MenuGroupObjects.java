package kitchenpos.utils.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.MenuGroup;

public class MenuGroupObjects {
    private final MenuGroup menuGroup1;
    private final MenuGroup menuGroup2;
    private final MenuGroup menuGroup3;
    private final MenuGroup menuGroup4;

    public MenuGroupObjects() {
        menuGroup1 = new MenuGroup();
        menuGroup2 = new MenuGroup();
        menuGroup3 = new MenuGroup();
        menuGroup4 = new MenuGroup();

        menuGroup1.setId(1L);
        menuGroup1.setName("두마리메뉴");
        menuGroup2.setId(2L);
        menuGroup2.setName("한마리메뉴");
        menuGroup3.setId(3L);
        menuGroup3.setName("순살파닭두마리메뉴");
        menuGroup4.setId(4L);
        menuGroup4.setName("신메뉴");
    }

    public MenuGroup getMenuGroup1() {
        return menuGroup1;
    }

    public MenuGroup getMenuGroup2() {
        return menuGroup2;
    }

    public MenuGroup getMenuGroup3() {
        return menuGroup3;
    }

    public MenuGroup getMenuGroup4() {
        return menuGroup4;
    }

    public List<MenuGroup> getMenuGroups() {
        return new ArrayList<>(Arrays.asList(menuGroup1, menuGroup2, menuGroup3, menuGroup4));
    }
}
