package kitchenpos.product.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.StringUtils;

@Embeddable
public class Name {

    @Column
    private String name;

    protected Name() {
    }

    private Name(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("이름이 null 또는 공백입니다.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Name of(String name) {
        return new Name(name);
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

    @Override
    public String toString() {
        return "Name{" +
            "name='" + name + '\'' +
            '}';
    }
}
