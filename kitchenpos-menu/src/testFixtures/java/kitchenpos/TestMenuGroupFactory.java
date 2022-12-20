package kitchenpos;

import kitchenpos.common.domain.Name;
import kitchenpos.menugroup.domain.MenuGroup;

public class TestMenuGroupFactory {
    public static MenuGroup create(Long id, String name) {
        return new MenuGroup(id, new Name(name));
    }
}
