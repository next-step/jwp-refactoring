package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupEntity;

public class MenuGroupResponse {
    private Long id;
    private String name;

    public MenuGroupResponse() {
    }

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(MenuGroupEntity menuGroupEntity) {
        return new MenuGroupResponse(menuGroupEntity.getId(), menuGroupEntity.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
