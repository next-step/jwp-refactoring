package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtureFactory {
    private MenuGroupFixtureFactory() {
    }

    public static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
