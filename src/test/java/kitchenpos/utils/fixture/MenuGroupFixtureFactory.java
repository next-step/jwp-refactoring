package kitchenpos.utils.fixture;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupFixtureFactory {
    public static MenuGroup createMenuGroup(Long id, String name) {
        return MenuGroup.of(id, name);
    }

    public static MenuGroup createMenuGroup(String name) {
        return MenuGroup.from(name);
    }
}
