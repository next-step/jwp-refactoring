package kitchenpos.menu.dto;

import kitchenpos.common.domain.Name;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest() {
    }

    private MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest of(String name) {
        return new MenuGroupRequest(name);
    }

    public MenuGroup toMenuGroup() {
        return MenuGroup.of(Name.of(name));
    }

    public String getName() {
        return name;
    }
}
