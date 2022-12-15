package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest() {}

    private MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest from(String name) {
        return new MenuGroupRequest(name);
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
