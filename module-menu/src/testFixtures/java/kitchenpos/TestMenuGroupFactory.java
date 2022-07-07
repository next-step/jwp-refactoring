package kitchenpos;

import kitchenpos.common.domain.Name;
import kitchenpos.menuGroup.domain.MenuGroup;

public class TestMenuGroupFactory {

    public static MenuGroup create(String name) {
        return create(null, name);
    }

    public static MenuGroup create(Long id, String name) {
        return new MenuGroup(id, new Name(name));
    }
}
