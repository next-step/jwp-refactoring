package kitchenpos.dto.menugroup;

import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupRequest {
    private String name;

    protected MenuGroupRequest() {}

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest from(String name) {
        return new MenuGroupRequest(name);
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return MenuGroup.from(name);
    }
}
