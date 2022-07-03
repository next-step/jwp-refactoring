package kitchenpos.menugroup.dto;

import java.util.Objects;
import kitchenpos.domain.Name;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupResponse {
    private final Long id;
    private final String name;

    private MenuGroupResponse(Long id, Name name) {
        this.id = id;
        this.name = name.value();
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.id(), menuGroup.name());
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
