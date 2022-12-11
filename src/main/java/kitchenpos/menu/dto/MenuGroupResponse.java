package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.common.domain.Name;

public class MenuGroupResponse {

    private Long id;
    private String name;

    private MenuGroupResponse() {}

    private MenuGroupResponse(Long id, Name name) {
        this.id = id;
        this.name = name.value();
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
