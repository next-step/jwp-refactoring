package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    private MenuGroupRequest() {
    }

    private MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest from (String name) {
        return new MenuGroupRequest(name);
    }

    public String getName() {
        return name;
    }
}
