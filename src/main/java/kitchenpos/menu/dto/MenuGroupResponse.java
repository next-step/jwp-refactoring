package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

import java.util.ArrayList;
import java.util.List;

public class MenuGroupResponse {
    private Long id;
    private String name;

    public MenuGroupResponse() {
    }

    public MenuGroupResponse(String name) {
        this.name = name;
    }

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.id(), menuGroup.name());
    }

    public static List<MenuGroupResponse> ofList(List<MenuGroup> menuGroups) {
        List<MenuGroupResponse> menuGroupResponses = new ArrayList<>();
        for (MenuGroup menuGroup : menuGroups) {
            menuGroupResponses.add(new MenuGroupResponse(menuGroup.id(), menuGroup.name()));
        }
        return menuGroupResponses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
