package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.exception.EmptyNameException;
import kitchenpos.utils.StringUtils;

@Embeddable
public class Name {

    @Column(name = "name", nullable = false)
    private String name;

    protected Name() {}

    private Name(String name) {
        this.name = name;
    }

    public static Name from(String name) {
        validateName(name);
        return new Name(name);
    }

    private static void validateName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new EmptyNameException(name);
        }
    }

    public String getValue() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Name name1 = (Name)o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
