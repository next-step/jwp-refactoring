package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return MenuGroup.from(null, name);
    }

    public String getName() {
        return name;
    }
}
