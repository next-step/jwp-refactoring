package kitchenpos.domain.menuGroup;

import kitchenpos.dto.menuGroup.MenuGroupRequest;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(MenuGroupRequest menuGroupRequest) {
        return new MenuGroup(menuGroupRequest.getId(), menuGroupRequest.getName());
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
