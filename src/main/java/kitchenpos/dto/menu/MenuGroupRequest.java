package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupRequest {

    private final String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.of(name);
    }

    public String getName() {
        return name;
    }
}
