package kitchenpos.menugroup.dto;

import java.util.List;

public class MenuGroupListResponse {
    List<MenuGroupResponse> menuGroupResponses;

    public MenuGroupListResponse() {}

    public MenuGroupListResponse(List<MenuGroupResponse> menuGroupResponses) {
        this.menuGroupResponses = menuGroupResponses;
    }

    public static MenuGroupListResponse of(List<MenuGroupResponse> menuGroupResponse) {
        return new MenuGroupListResponse(menuGroupResponse);
    }

    public List<MenuGroupResponse> getMenuGroupResponses() {
        return menuGroupResponses;
    }
}
