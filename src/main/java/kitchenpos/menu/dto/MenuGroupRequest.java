package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {
    private final Long id;
    private final String name;

    public MenuGroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
