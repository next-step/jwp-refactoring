package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static kitchenpos.utils.Message.INVALID_NAME_EMPTY;

@Embeddable
public class MenuName {

    @Column(nullable = false)
    private String name;

    protected MenuName() {
    }

    private MenuName(String name) {
        this.name = name;
    }

    public static MenuName from(String name) {
        Validator.checkNotNull(name, INVALID_NAME_EMPTY);
        return new MenuName(name);
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
        return name.equals(menuName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
