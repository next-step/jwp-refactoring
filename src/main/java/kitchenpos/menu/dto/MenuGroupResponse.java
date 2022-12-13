package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class MenuGroupResponse {
    private Long id;
    private String name;

    public MenuGroupResponse() {}

    public MenuGroupResponse(MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup);
    }

    public static List<MenuGroupResponse> list(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::new)
                .collect(toList());
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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
