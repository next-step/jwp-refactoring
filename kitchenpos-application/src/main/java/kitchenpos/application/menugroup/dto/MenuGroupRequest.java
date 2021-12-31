package kitchenpos.application.menugroup.dto;

import kitchenpos.core.domain.MenuGroup;

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
