package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuGroupName {
    @Column(nullable = false)
    private String name;

    protected MenuGroupName() {
    }

    private MenuGroupName(String name) {
        this.name = name;
    }

    public static MenuGroupName from(String name) {
        return new MenuGroupName(name);
    }

    public String getValue() {
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
        MenuGroupName that = (MenuGroupName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
