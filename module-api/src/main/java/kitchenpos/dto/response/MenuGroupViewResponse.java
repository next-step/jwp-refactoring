package kitchenpos.dto.response;

import kitchenpos.domain.menu.MenuGroup;

import java.util.Objects;

public class MenuGroupViewResponse {
    private Long id;
    private String name;

    public static MenuGroupViewResponse of(MenuGroup menuGroup) {
        return new MenuGroupViewResponse(menuGroup.getId(),
                menuGroup.getName().toString());
    }

    protected MenuGroupViewResponse() {
    }

    public MenuGroupViewResponse(Long id, String name) {
        this.id = id;
        this.name = name;
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
        MenuGroupViewResponse response = (MenuGroupViewResponse) o;
        return Objects.equals(id, response.id) && Objects.equals(name, response.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
