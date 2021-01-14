package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupDto {
    private Long id;
    private String name;

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
