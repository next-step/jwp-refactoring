package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.menugroup.MenuGroup;

public class MenuGroupDomainFixture {

    public static MenuGroup menuGroup(String name) {
        return new MenuGroup(name);
    }

}
