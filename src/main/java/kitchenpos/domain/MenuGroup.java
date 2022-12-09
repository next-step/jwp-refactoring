package kitchenpos.domain;

import java.util.Objects;

public class MenuGroup {
    private Long id;
    private String name;

    private MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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
