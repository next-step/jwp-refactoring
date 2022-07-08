package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuGroup;

import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupResponse {
    private Long id;
    private String name;

    protected MenuGroupResponse() {
    }

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> toMenuGroupResponses(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
