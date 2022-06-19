package kitchenpos.application.fixture;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupFixtureFactory {

    private MenuGroupFixtureFactory() {}

    public static MenuGroup create(Long id, String name) {
        return MenuGroup.of(id, name);
    }

    public static MenuGroup create(String name) {
        return MenuGroup.from(name);
    }
}
