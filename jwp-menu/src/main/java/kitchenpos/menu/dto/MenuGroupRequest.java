package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    protected MenuGroupRequest() {
    }

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public static MenuGroupRequest from(final String name) {
        return new MenuGroupRequest(name);
    }

    public MenuGroup toMenuGroup() {
        return MenuGroup.from(name);
    }

    public String getName() {
        return name;
    }
}
