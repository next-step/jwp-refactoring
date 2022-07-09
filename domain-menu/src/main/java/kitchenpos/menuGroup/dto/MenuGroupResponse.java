package kitchenpos.menuGroup.dto;

import kitchenpos.menuGroup.domain.MenuGroup;

public class MenuGroupResponse {
    private Long menuGroupId;
    private String name;

    public MenuGroupResponse() {
    }

    public MenuGroupResponse(final MenuGroup menuGroup) {
        this.menuGroupId = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public String getName() {
        return name;
    }
}
