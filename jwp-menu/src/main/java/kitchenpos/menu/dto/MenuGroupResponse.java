package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupResponse {
    private Long id;
    private String name;

    protected MenuGroupResponse() {
    }

    private MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> from(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }

    public static MenuGroupResponse of(final Long id, final String name) {
        return new MenuGroupResponse(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
