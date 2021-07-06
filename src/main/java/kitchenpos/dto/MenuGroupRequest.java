package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(null, name);
    }
}
