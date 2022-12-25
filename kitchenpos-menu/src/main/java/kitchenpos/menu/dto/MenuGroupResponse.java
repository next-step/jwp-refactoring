package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupResponse {
    private Long id;
    private String name;

    public MenuGroupResponse() {}

    public MenuGroupResponse(MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup);
    }

    public static List<MenuGroupResponse> toResponselist(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
