package kitchenpos.product.ui.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.MenuGroup;

public final class MenuGroupResponse {

    private long id;
    private String name;

    private MenuGroupResponse() {
    }

    private MenuGroupResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.id(), menuGroup.name().value());
    }

    public static List<MenuGroupResponse> listFrom(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
