package kitchenpos.menu.domain.fixture;

import kitchenpos.common.Name;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    public static final String MENU_GROUP_A = "메뉴그룹 A";

    public static MenuGroup menuGroupA() {
        return new MenuGroup(new Name(MENU_GROUP_A));
    }
}
