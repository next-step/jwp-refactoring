package kitchenpos.common.vo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.exception.ErrorMessage;

@Embeddable
public class Name {
    public static String PROPERTY_NAME = "이름";
    @Column(name = "name", nullable = false)
    private String value;

    protected Name() {}

    private Name(String name) {
        validateNameIsNull(name);
        this.value = name;
    }

    public static Name of(String name) {
        return new Name(name);
    }

    private void validateNameIsNull(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.cannotBeNull(PROPERTY_NAME));
        }
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Name)) {
            return false;
        }
        Name name = (Name)o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
