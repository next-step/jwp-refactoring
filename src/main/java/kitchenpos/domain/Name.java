package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.EmptyNameException;
import kitchenpos.utils.StringUtil;

@Embeddable
public class Name {
    @Column(name = "name", nullable = false)
    private String value;

    protected Name() {}

    private Name(String name) {
        this.value = name;
    }

    public static Name from(String name) {
        validateName(name);
        return new Name(name);
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
        Name name = (Name) o;
        return Objects.equals(getValue(), name.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
