package kitchenpos.dto;

import kitchenpos.domain.model.MenuGroup;

public class MenuGroupDto {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuGroupDto() {
    }

    public MenuGroupDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupDto of(MenuGroup menuGroup) {
        return new MenuGroupDto(menuGroup.getId(), menuGroup.getName());
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }
}
