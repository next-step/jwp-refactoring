package menu.dto;

import menu.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    protected MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest of(String name) {
        return new MenuGroupRequest(name);
    }

    public MenuGroup toEntity() {
        return MenuGroup.of(name);
    }

    public String getName() {
        return name;
    }
}
