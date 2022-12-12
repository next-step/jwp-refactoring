package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.NameValidator;
import kitchenpos.exception.ExceptionMessage;

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
        NameValidator.checkNotNull(name, ExceptionMessage.INVALID_MENU_GROUP_NAME_SIZE);
        return new MenuGroupName(name);
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
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
