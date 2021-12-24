package kitchenpos.common.fixtrue;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    private MenuGroupFixture() {

    }

    public static MenuGroup of(long id, String name) {
        return MenuGroup.of(id, name);
    }
}
