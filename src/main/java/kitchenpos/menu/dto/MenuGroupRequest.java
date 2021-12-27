package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

import javax.validation.constraints.NotNull;

public class MenuGroupRequest {
    @NotNull
    private String name;

    public MenuGroupRequest() {
    }

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
