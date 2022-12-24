package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Name;

public class MenuGroupFixture {

    public static final String MENU_GROUP_A = "메뉴그룹 A";

    public static MenuGroup menuGroupA() {
        return new MenuGroup(new Name(MENU_GROUP_A));
    }
}
