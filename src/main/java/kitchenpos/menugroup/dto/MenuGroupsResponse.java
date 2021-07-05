package kitchenpos.menugroup.dto;

import java.util.List;

public class MenuGroupsResponse {
    List<MenuGroupResponse> menuGroupResponses;

    public MenuGroupsResponse() {}

    public MenuGroupsResponse(List<MenuGroupResponse> menuGroupResponses) {
        this.menuGroupResponses = menuGroupResponses;
    }

    public static MenuGroupsResponse of(List<MenuGroupResponse> menuGroupResponse) {
        return new MenuGroupsResponse(menuGroupResponse);
    }

    public List<MenuGroupResponse> getMenuGroupResponses() {
        return menuGroupResponses;
    }
}
