package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup create(String name) {
        return new MenuGroup(name);
    }

}
