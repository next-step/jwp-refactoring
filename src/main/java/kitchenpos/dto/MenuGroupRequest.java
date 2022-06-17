package kitchenpos.dto;

import kitchenpos.domain.MenuGroupEntity;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    protected MenuGroupRequest() {
    }

    public String getName() {
        return name;
    }

    public MenuGroupEntity toMenuGroup() {
        return new MenuGroupEntity(name);
    }
}
