package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest of(MenuGroup menuGroup) {
        return new MenuGroupRequest(menuGroup.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }

    @Override
    public String toString() {
        return "MenuGroupRequest{" +
            "name='" + name + '\'' +
            '}';
    }
}
