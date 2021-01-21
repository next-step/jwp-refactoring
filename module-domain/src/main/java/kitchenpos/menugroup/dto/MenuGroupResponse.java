package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.MenuGroup;

public class MenuGroupResponse {
    private Long id;
    private String name;

    public MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroupResponse(final MenuGroup menuGroup) {
        this(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public MenuGroupResponse setId(final Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuGroupResponse setName(final String name) {
        this.name = name;
        return this;
    }
}
