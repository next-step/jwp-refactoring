package kitchenpos.factory;

import kitchenpos.menuGroup.domain.MenuGroup;

public class MenuGroupFixtureFactory {
    public static MenuGroup createMenuGroup(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup createMenuGroup(String name) {
        return new MenuGroup(name);
    }
}
