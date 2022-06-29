package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

import java.util.Objects;

public class MenuGroupResponse {
    private final Long id;
    private final String name;

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuGroupResponse that = (MenuGroupResponse) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
