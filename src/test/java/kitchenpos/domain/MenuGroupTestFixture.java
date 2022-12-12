package kitchenpos.domain;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupTestFixture {
    public static MenuGroup menuGroup(Long id, String name) {
        return MenuGroup.from(id, name);
    }
}
