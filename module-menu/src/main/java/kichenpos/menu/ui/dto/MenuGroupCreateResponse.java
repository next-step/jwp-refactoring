package kichenpos.menu.ui.dto;

import kichenpos.menu.domain.MenuGroup;

public class MenuGroupCreateResponse {
    private Long id;
    private String name;

    private MenuGroupCreateResponse() {
    }

    public MenuGroupCreateResponse(MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
