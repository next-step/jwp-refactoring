package kitchenpos.menu.group.dto;

import kitchenpos.menu.group.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.create(name);
    }
}
