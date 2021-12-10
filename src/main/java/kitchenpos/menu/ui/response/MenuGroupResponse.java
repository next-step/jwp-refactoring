package kitchenpos.menu.ui.response;

import kitchenpos.domain.MenuGroup;

public final class MenuGroupResponse {

    private long id;
    private String name;

    private MenuGroupResponse() {
    }

    private MenuGroupResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return null;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
