package kichenpos.menu.ui.dto;

import kichenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {
    private Long id;
    private String name;

    private MenuGroupResponse() {
    }

    public MenuGroupResponse(MenuGroup menuGroup) {
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
