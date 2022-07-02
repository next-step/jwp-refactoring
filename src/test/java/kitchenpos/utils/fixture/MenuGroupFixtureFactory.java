package kitchenpos.utils.fixture;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupFixtureFactory {
    public static MenuGroup createMenuGroup(String name) {
        return MenuGroup.from(name);
    }
}
