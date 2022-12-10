package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupTestFixture {

    public static MenuGroup generateMenuGroup(Long id, String name) {
        return MenuGroup.of(id, name);
    }

    public static MenuGroup generateMenuGroup(String name) {
        return MenuGroup.of(null, name);
    }

    public static MenuGroupRequest generateMenuGroupRequest(Name name) {
        return new MenuGroupRequest(name.value());
    }

    public static MenuGroupRequest generateMenuGroupRequest(String name) {
        return new MenuGroupRequest(name);
    }
}
