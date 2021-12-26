package kitchenpos.menu.group.dto;

import kitchenpos.menu.group.domain.MenuGroup;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuGroupResponse {

    private Long id;
    private String name;

    public MenuGroupResponse() {

    }

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> ofList(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::of)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
