package kitchenpos.core;

import kitchenpos.core.domain.MenuGroup;

public class MenuGroupFixture {
    private MenuGroupFixture() {
    }

    public static MenuGroup getMenuGroup(long id, String name) {
        return MenuGroup.generate(id, name);
    }
}
