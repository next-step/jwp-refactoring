package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * packageName : kitchenpos.application
 * fileName : MenuGroupResponse
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class MenuGroupResponse {
    private Long id;
    private String name;

    private MenuGroupResponse() {
    }

    public MenuGroupResponse(Long id, String name) {
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
