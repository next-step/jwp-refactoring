package kitchenpos.common.valueobject;

import kitchenpos.common.valueobject.exception.InvalidNameException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {

    @Column(name = "name", nullable = false)
    private final String value;

    protected Name() {
        this.value = null;
    }

    private Name(String value) {
        validateName(value);
        this.value = value;
    }

    private void validateName(String value) {
        if (Objects.isNull(value) || Objects.equals(value, "")) {
            throw new InvalidNameException();
        }
    }

    public static Name of(String value) {
        return new Name(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name1 = (Name) o;
        return Objects.equals(value, name1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
