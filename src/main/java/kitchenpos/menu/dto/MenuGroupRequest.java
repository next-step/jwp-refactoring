package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest() {
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.of(name);
    }
}
