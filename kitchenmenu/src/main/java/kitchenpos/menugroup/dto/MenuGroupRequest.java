package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public static MenuGroupRequest from(MenuGroup menuGroup) {
        return new MenuGroupRequest(menuGroup.getName());
    }

    public MenuGroupRequest() {}

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }

    public MenuGroup createMenuGroup() {
        return MenuGroup.create(name);
    }

    public String getName() {
        return name;
    }
}
