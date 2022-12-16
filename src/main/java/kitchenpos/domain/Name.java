package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {
    private String name;

    public Name(String name) {
        this.name = name;
    }

    public static Name from(String name) {
        validLength(name);
        return new Name(name);
    }

    private static void validLength(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("이름은 한 글자 이상이어야 합니다");
        }
    }

    protected Name() {
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
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
