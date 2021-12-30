package kitchenpos.menus.menugroup.dto;

import kitchenpos.common.domain.Name;
import kitchenpos.menus.menugroup.domain.MenuGroup;

public class MenuGroupRequest {

    private final String name;

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(new Name(name));
    }

    public String getName() {
        return name;
    }
}
