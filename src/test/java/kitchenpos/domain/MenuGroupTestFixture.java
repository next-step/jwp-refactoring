package kitchenpos.domain;

import kitchenpos.dto.MenuGroupRequest;

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
