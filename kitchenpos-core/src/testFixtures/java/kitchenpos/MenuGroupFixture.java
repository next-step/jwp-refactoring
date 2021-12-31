package kitchenpos;

import kitchenpos.domain.menugroup.domain.MenuGroup;

public class MenuGroupFixture {
    private MenuGroupFixture() {
    }

    public static MenuGroup getMenuGroup(long id, String name) {
        return MenuGroup.generate(id, name);
    }
}
