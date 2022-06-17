package kitchenpos.dto;

import kitchenpos.domain.MenuGroupEntity;
import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupResponse {
    private Long id;
    private String name;

    protected MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    protected MenuGroupResponse() {
    }

    public static MenuGroupResponse of(MenuGroupEntity menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName().getValue());
    }

    public static List<MenuGroupResponse> of(List<MenuGroupEntity> menuGroups) {
        return menuGroups.stream()
                         .map(MenuGroupResponse::of)
                         .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
