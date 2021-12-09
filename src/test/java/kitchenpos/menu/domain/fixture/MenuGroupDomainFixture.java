package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.menugroup.MenuGroup;

public class MenuGroupDomainFixture {

    public static MenuGroup 한마리_메뉴 = menuGroup("한마리메뉴");

    public static MenuGroup menuGroup(String name) {
        return new MenuGroup(name);
    }

}
