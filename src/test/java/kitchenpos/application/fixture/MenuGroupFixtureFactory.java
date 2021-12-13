package kitchenpos.application.fixture;

import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupFixtureFactory {

    private MenuGroupFixtureFactory() {}

    public static MenuGroup create(long id, String name) {

        return MenuGroup.of(id, name);
    }
}
