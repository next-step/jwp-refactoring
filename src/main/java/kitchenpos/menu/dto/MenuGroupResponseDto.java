package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponseDto {
    private Long id;
    private String name;

    public MenuGroupResponseDto(MenuGroup menuGroup) {
        this(menuGroup.getId(), menuGroup.getName());
    }

    public MenuGroupResponseDto(Long id, String name) {
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
