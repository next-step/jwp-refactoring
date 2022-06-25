package kitchenpos.factory;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtureFactory {
    public static MenuGroup createMenuGroup(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
