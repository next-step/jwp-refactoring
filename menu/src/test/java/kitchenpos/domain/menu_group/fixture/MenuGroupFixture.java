package kitchenpos.domain.menu_group.fixture;

import kitchenpos.domain.menu_group.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup 메뉴_그룹() {
        return 메뉴_그룹("추천메뉴");
    }
}
