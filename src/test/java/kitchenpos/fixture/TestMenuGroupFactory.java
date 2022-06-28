package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class TestMenuGroupFactory {

    public static MenuGroup create(String name) {
        return create(null, name);
    }

    public static MenuGroup create(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
