package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest() {}

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
