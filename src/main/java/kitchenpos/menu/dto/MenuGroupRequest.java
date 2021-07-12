package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    public String name;

    protected MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MenuGroupRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
