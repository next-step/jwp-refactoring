package kichenpos.menu.ui.dto;

import kichenpos.menu.domain.MenuGroup;

public class MenuGroupCreateRequest {
    private String name;

    private MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }
}
