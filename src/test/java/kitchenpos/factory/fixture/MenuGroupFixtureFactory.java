package kitchenpos.factory.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixtureFactory {
    public static MenuGroup createMenuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
