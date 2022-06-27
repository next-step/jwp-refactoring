package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.menu.exception.EmptyNameException;
import kitchenpos.menu.utils.StringUtil;

@Embeddable
public class MenuName {
    @Column(name = "name", nullable = false)
    private String value;

    protected MenuName() {}

    private MenuName(String name) {
        this.value = name;
    }

    public static MenuName from(String name) {
        validateName(name);
        return new MenuName(name);
    }

    public String getValue() {
        return this.value;
    }

    private static void validateName(String name) {
        if (StringUtil.isEmpty(name)) {
            throw new EmptyNameException(name);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuName name = (MenuName) o;
        return Objects.equals(getValue(), name.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
