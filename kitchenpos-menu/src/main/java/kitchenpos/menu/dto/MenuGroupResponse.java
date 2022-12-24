package kitchenpos.menu.dto;

import kitchenpos.domain.Name;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponse {

    private long id;
    private String name;

    protected MenuGroupResponse() {
    }

    public MenuGroupResponse(long id, Name name) {
        this.id = id;
        this.name = name.value();
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
