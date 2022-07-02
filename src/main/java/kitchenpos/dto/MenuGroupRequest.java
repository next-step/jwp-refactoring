package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    protected MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}