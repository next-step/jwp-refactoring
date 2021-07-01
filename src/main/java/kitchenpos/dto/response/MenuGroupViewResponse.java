package kitchenpos.dto.response;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupViewResponse {
    private Long id;
    private String name;

    public static MenuGroupViewResponse of(MenuGroup menuGroup) {
        return new MenuGroupViewResponse(menuGroup.getId(),
                menuGroup.getName().toString());
    }

    protected MenuGroupViewResponse() {
    }

    public MenuGroupViewResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
