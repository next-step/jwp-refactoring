package kitchenpos.menuGroup.dto;

import kitchenpos.menuGroup.domain.MenuGroup;

public class MenuGroupCreateRequest {
    private String name;

    protected MenuGroupCreateRequest() {}

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public MenuGroup of() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
