package kitchenpos.menu.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    private MenuGroupFixture() {
    }

    public static MenuGroup create(final Long id, final String menuGroupName) {
        return new MenuGroup(id, menuGroupName);
    }
}
