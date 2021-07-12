package kitchenpos.menu.dto.dto;

import kitchenpos.menu.dto.MenuGroupDto;

public class MenuGroupResponse {

    private Long id;

    private String name;

    protected MenuGroupResponse() { }

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroupResponse(String name) {
        this.name = name;
    }

    public static MenuGroupResponse of(MenuGroupDto menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
