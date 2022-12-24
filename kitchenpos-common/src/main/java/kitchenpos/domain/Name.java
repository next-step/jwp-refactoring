package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String name;

    protected Name() {
    }

    private Name(String name) {
        validName(name);
        this.name = name;
    }

    public static Name from(String name) {
        return new Name(name);
    }

    public String value() {
        return name;
    }

    private void validName(String name) {
        if (name == null || name.isEmpty() || name.trim().isEmpty()) {
            throw new IllegalArgumentException("이름을 확인하세요.");
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

        Name name1 = (Name) o;

        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
