package kitchenpos.menu.group.dto;

import kitchenpos.menu.group.domain.MenuGroup;

import javax.validation.constraints.NotNull;

public class MenuGroupRequest {

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.create(name);
    }
}
