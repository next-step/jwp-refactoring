package kitchenpos.menu.group.dto;

import kitchenpos.menu.group.domain.MenuGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MenuGroupRequest {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.create(name);
    }
}
