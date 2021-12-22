package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroupRequest() {
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return MenuGroup.of(name);
    }
}
