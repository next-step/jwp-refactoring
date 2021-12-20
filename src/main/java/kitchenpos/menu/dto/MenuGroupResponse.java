package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

import java.util.Objects;

public class MenuGroupResponse {
    private Long id;
    private String name;

    protected MenuGroupResponse() {
    }

    public MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuGroupResponse that = (MenuGroupResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
