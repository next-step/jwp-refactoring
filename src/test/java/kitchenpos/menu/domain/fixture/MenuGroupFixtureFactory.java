package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixtureFactory {
    private MenuGroupFixtureFactory() {
    }

    public static MenuGroup createMenuGroup(String name) {
        return new MenuGroup(name);
    }
}
