package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuGroup;

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
