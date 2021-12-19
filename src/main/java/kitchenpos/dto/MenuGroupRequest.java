package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }


    public static MenuGroupRequest from(MenuGroup menuGroup) {
        return new MenuGroupRequest(menuGroup.getName());
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

}
