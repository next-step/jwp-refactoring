package kitchenpos.utils.fixture;

import kitchenpos.menuGroup.domain.MenuGroup;

public class MenuGroupFixtureFactory {
    public static MenuGroup createMenuGroup(String name) {
        return MenuGroup.from(name);
    }
}
