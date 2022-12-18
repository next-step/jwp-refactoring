package kitchenpos.menugroup.testfixture;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupTestFixture {

    public static MenuGroup create(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup create(String name) {
        return new MenuGroup(null, name);
    }
}
