package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    protected MenuGroupRequest() {
    }

    protected MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest from(String name) {
        return new MenuGroupRequest(name);
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
