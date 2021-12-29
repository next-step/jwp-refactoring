package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

import java.util.ArrayList;
import java.util.List;

public class MenuGroupResponse {

    private Long id;
    private String name;

    public MenuGroupResponse() {
    }

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> ofList(List<MenuGroup> menuGroups) {
        List<MenuGroupResponse> menuGroupResponses = new ArrayList<>();
        for (MenuGroup menuGroup : menuGroups) {
            menuGroupResponses.add(MenuGroupResponse.of(menuGroup));
        }
        return menuGroupResponses;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
