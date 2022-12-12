package kitchenpos.common.domain;

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
        validateNameNotEmpty(name);
        this.name = name;
    }

    public static Name from(String name) {
        return new Name(name);
    }

    private void validateNameNotEmpty(String name) {
        if(Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.NAME_NOT_EMPTY.getErrorMessage());
        }
    }

    public String value() {
        return name;
    }
}
