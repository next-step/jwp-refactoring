package kitchenpos.common;

import org.apache.logging.log4j.util.Strings;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {

    public static final String NAME_NOT_EMPTY_EXCEPTION_MESSAGE = "이름은 null 이나 공백일 수 없습니다.";

    private String name;

    protected Name() {
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

    public Name(String name) {
        validateName(name);
        this.name = name;
    }

    private static void validateName(String name) {
        if (Strings.isEmpty(name)) {
            throw new IllegalArgumentException(NAME_NOT_EMPTY_EXCEPTION_MESSAGE);
        }
    }
}
