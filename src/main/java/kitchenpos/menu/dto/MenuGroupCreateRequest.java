package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.of(name);
    }

    public String getName() {
        return name;
    }
}
