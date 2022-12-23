package kitchenpos.menu.dto.response;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {
    private final Long id;
    private final String name;

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(Long id, String name) {
        return new MenuGroupResponse(id, name);
    }

    public static MenuGroupResponse of(MenuGroup menuGroup){
        return MenuGroupResponse.of(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
