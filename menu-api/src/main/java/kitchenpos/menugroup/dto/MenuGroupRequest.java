package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {
    private final Long id;
    private final String name;

    public MenuGroupRequest(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupRequest of(final Long id, final String name) {
        return new MenuGroupRequest(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return MenuGroup.of(name);
    }
}
