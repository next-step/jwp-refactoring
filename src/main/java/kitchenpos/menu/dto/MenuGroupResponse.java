package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuGroupResponse {
    private Long id;
    private String name;

    public MenuGroupResponse() {
    }

    public MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> fromToList(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
