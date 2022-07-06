package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    public MenuGroupResponse() {

    }

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup persistMenuGroup) {
        return new MenuGroupResponse(persistMenuGroup.getId(), persistMenuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
