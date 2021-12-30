package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuGroupResponse {

    private Long id;
    private String name;

    private MenuGroupResponse() {
    }

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> fromList(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList());
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
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(),
            that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
