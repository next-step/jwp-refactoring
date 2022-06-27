package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class TestMenuGroupFactory {

    public static MenuGroup create(String name) {
        return create(null, name);
    }

    public static MenuGroup create(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
