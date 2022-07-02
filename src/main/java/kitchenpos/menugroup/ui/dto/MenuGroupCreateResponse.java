package kitchenpos.menugroup.ui.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupCreateResponse {
    private Long id;
    private String name;

    private MenuGroupCreateResponse() {
    }

    public MenuGroupCreateResponse(MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
