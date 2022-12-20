package kitchenpos.menu.domain;

import kitchenpos.product.domain.Name;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MenuName implements Name {

    private String name;

    private MenuName(String name) {
        validLength(name);
        this.name = name;
    }

    public static MenuName from(String name) {
        return new MenuName(name);
    }

    public MenuName() {
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
        MenuName menuName = (MenuName) o;
        return Objects.equals(name, menuName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
