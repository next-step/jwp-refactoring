package kitchenpos.menu.domain;

import javax.persistence.Column;
import java.util.Objects;

public class Name {
    @Column(name = "name")
    private final String value;

    public Name() {
        this("");
    }

    public Name(String name) {
        this.value = name;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
