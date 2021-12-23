package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

import java.util.List;

public class MenuGroupResponse {
    private Long id;
    private String name;

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(Long id, String name) {
        return new MenuGroupResponse(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
