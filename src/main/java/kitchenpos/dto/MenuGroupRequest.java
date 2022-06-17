package kitchenpos.dto;

import kitchenpos.domain.MenuGroupEntity;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    protected MenuGroupRequest() {
    }

    public MenuGroupEntity toMenuGroup() {
        return new MenuGroupEntity(name);
    }

    public String getName() {
        return name;
    }
}
