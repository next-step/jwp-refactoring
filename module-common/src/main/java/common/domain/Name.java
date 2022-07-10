package common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Embeddable
public class Name {

    @Column(nullable = false)
    private String name;

    protected Name() {
    }

    public Name(String name) {
        this.name = requireNonNull(name, "name");
    }

    public String getValue() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(name, name.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
