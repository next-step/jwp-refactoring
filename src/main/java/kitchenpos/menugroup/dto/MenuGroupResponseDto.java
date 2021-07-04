package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupResponseDto {
    private Long id;
    private String name;

    public MenuGroupResponseDto() {}

    public MenuGroupResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponseDto of(MenuGroup menuGroup) {
        return new MenuGroupResponseDto(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
