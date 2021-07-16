package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest() {
        // empty
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(this.name);
    }
}
