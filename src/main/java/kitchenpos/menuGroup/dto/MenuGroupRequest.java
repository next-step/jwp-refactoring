package kitchenpos.menuGroup.dto;

import kitchenpos.menuGroup.domain.MenuGroup;

public class MenuGroupRequest{
    private String name;

    protected MenuGroupRequest() {
    }

    private MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest from(String name) {
        return new MenuGroupRequest(name);
    }

    public String getName() {
        return name;
    }
}
