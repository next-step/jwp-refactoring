package kitchenpos.menu.application.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupDtoFixtureFactory {
    private MenuGroupDtoFixtureFactory() {
    }

    public static MenuGroup createMenuGroup(String name) {
        return new MenuGroup(name);
    }
}
