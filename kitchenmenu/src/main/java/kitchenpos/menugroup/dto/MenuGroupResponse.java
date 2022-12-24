package kitchenpos.menugroup.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> getMenuGroupResponses(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }

    public MenuGroupResponse() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuGroupResponse that = (MenuGroupResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
