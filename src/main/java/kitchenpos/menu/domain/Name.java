package kitchenpos.menu.domain;

import org.springframework.util.ObjectUtils;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {
    private String name;

    protected Name() {
    }

    public Name(String name) {
        this.name = name;
    }

    public static Name of(String name) {
        if(ObjectUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name을 입력하세요");
        }
        return new Name(name);
    }

    public String value() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
