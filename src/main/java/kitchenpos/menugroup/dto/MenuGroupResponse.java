package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupResponse {
    private Long id;
    private String name;

    public MenuGroupResponse() {}

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(Long id, String name) {
        return new MenuGroupResponse(id, name);
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        String name = menuGroup.getName();
        return new MenuGroupResponse(menuGroup.getId(), name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
