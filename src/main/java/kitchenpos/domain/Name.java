package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.constant.ErrorCode;

@Embeddable
public class Name {

    @Column(nullable = false)
    private String name;

    protected Name() {}

    private Name(String name) {
        validateNameIsNull(name);
        this.name = name;
    }

    public static Name from(String name) {
        return new Name(name);
    }

    private void validateNameIsNull(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.이름은_비어_있을_수_없음.getErrorMessage());
        }
    }

    public String value() {
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
