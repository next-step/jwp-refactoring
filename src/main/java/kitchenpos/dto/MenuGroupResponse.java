package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

import java.util.List;

import static java.util.stream.Collectors.toList;

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

    public static List<MenuGroupResponse> list(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::new)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
