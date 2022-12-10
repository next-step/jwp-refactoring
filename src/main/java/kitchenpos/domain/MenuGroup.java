package kitchenpos.domain;

import java.util.Objects;

public class MenuGroup {
    private Long id;
    private MenuGroupName name;

    private MenuGroup() {
    }

    private MenuGroup(Long id, MenuGroupName name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, MenuGroupName.from(name));
    }

    public static MenuGroup from(String name) {
        return new MenuGroup(null, MenuGroupName.from(name));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuGroup menuGroup = (MenuGroup) o;
        return id.equals(menuGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
