package kitchenpos.ui.fixtrue;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    private MenuGroupFixture() {

    }

    public static MenuGroup of(long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
