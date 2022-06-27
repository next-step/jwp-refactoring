package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {
    private Long id;
    private String name;

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        MenuGroupResponse response = new MenuGroupResponse();

        response.id = menuGroup.getId();
        response.name = menuGroup.getName();

        return response;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
