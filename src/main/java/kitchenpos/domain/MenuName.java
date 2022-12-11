package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.NameValidator;
import kitchenpos.exception.ExceptionMessage;

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
        NameValidator.checkNotNull(name, ExceptionMessage.INVALID_MENU_NAME_SIZE);
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
