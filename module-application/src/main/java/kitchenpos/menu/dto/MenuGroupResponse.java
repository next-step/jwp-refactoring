package kitchenpos.menu.dto;

import kitchenpos.menu.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    protected MenuGroupResponse() {}

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
