package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    private MenuGroupFixture() {

    }

    public static MenuGroup from(String name) {
        return MenuGroup.from(name);
    }
}
