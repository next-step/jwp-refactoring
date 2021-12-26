package kitchenpos.tobe.menus.dto;

import kitchenpos.tobe.common.domain.Name;
import kitchenpos.tobe.menus.domain.MenuGroup;

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
