package kitchenpos.dto.request;

import kitchenpos.domain.menus.menuGroup.domain.MenuGroup;

public class MenuGroupRequest {
    private Long id;
    private String name;

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }
}
