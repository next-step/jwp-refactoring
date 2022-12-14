package kitchenpos.domain;

import java.util.Objects;

public class MenuGroup {
    private Long id;
    private String name;

    private MenuGroup(String name) {

        this.name = name;
    }

    public static MenuGroup from(String name) {
        validEmptyName(name);
        return new MenuGroup(name);
    }

    private static void validEmptyName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("이름은 빈 값이 아니어야 합니다");
        }
    }

    public MenuGroup() {
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
        return Objects.equals(name, menuGroup.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
