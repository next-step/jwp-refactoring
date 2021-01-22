package kitchenpos.dto;

import kitchenpos.domain.menu.MenuGroup;

import javax.validation.constraints.NotBlank;

public class MenuGroupRequest {
    @NotBlank
    private String name;

    @SuppressWarnings("unused")
    protected MenuGroupRequest() {}

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
