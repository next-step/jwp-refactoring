package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    private MenuGroupRequest() {}

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return MenuGroup.of(name);
    }

    public String getName() {
        return name;
    }
}
