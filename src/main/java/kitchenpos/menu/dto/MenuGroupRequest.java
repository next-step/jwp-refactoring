package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupRequest {

    String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest of(MenuGroup menuGroup) {
        return new MenuGroupRequest(menuGroup.getName());
    }

    public static List<MenuGroup> toMenuGroupList(List<MenuGroupRequest> list) {
        return list.stream()
                .map(MenuGroupRequest::toMenuGroup)
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(this.name);
    }
}
