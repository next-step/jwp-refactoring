package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    private MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest of(String name) {
        return new MenuGroupRequest(name);
    }

    public MenuGroup toEntity() {
        return MenuGroup.of(name);
    }
}
