package kitchenpos.dto;

import java.util.Objects;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupDto {
    private Long id;
    private String name;

    protected MenuGroupDto() {
    }

    private MenuGroupDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupDto of(Long id, String name) {
        return new MenuGroupDto(id, name);
    }

    public static MenuGroupDto of(String name) {
        return new MenuGroupDto(null, name);
    }

    public static MenuGroupDto of(MenuGroup menuGroup) {
        return new MenuGroupDto(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return this.id;
    }

    public MenuGroup toMenuGroup() {
        return MenuGroup.of(this.name);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MenuGroupDto)) {
            return false;
        }
        MenuGroupDto menuGroupDto = (MenuGroupDto) o;
        return Objects.equals(id, menuGroupDto.id) && Objects.equals(name, menuGroupDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
