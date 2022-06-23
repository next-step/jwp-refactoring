package kitchenpos.dto.menuGroup;

import kitchenpos.domain.menuGroup.MenuGroup;

public class MenuGroupRequest {
    private final Long id;
    private final String name;

    public MenuGroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupRequest of(MenuGroup menuGroup) {
        return new MenuGroupRequest(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
