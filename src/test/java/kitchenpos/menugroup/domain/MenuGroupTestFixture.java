package kitchenpos.menugroup.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.menugroup.dto.MenuGroupRequest;

public class MenuGroupTestFixture {

    public static MenuGroup generateMenuGroup(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup generateMenuGroup(String name) {
        return new MenuGroup(null, name);
    }

    public static MenuGroupRequest generateMenuGroupRequest(Name name) {
        return new MenuGroupRequest(name.value());
    }

    public static MenuGroupRequest generateMenuGroupRequest(String name) {
        return new MenuGroupRequest(name);
    }
}
