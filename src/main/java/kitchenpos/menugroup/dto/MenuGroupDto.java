package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupDto {

    private Long id;

    private String name;

    protected MenuGroupDto() { }

    public MenuGroupDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroupDto(String name) {
        this.name = name;
    }

    public static MenuGroupDto of(MenuGroup menuGroup) {
        return new MenuGroupDto(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
