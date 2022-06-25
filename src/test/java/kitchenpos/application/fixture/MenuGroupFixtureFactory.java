package kitchenpos.application.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixtureFactory {

    private MenuGroupFixtureFactory() {
    }

    public static MenuGroup create(final Long id, final String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup create(final String name) {
        return new MenuGroup(name);
    }
}
