package kitchenpos.menu.domain;

import kitchenpos.product.domain.Name;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MenuGroupName implements Name {

    private String name;

    private MenuGroupName(String name) {
        validLength(name);
        this.name = name;
    }

    public static MenuGroupName from(String name) {
        return new MenuGroupName(name);
    }

    public MenuGroupName() {
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
        MenuGroupName that = (MenuGroupName) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
