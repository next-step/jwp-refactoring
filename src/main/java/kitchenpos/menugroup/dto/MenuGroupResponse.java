package kitchenpos.menugroup.dto;

import java.util.List;

public class MenuGroupResponse {
    List<MenuGroupResponseDto> menuGroupResponses;

    public MenuGroupResponse() {}

    public MenuGroupResponse(List<MenuGroupResponseDto> menuGroupResponses) {
        this.menuGroupResponses = menuGroupResponses;
    }

    public static MenuGroupResponse of(List<MenuGroupResponseDto> menuGroupResponse) {
        return new MenuGroupResponse(menuGroupResponse);
    }

    public List<MenuGroupResponseDto> getMenuGroupResponses() {
        return menuGroupResponses;
    }
}
