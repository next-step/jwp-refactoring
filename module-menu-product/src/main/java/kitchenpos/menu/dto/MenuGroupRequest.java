package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupName;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    protected MenuGroupRequest() {
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(MenuGroupName.from(name));
    }

    public String getName() {
        return name;
    }
}
