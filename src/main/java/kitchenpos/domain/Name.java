package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.InvalidParameterException;

@Embeddable
public class Name {
    private static final String ERROR_MESSAGE_NAME_IS_NOT_NULL = "이름은 필수입니다.";

    @Column(name = "name", nullable = false)
    private String value;

    protected Name() {}

    private Name(String value) {
        validateNull(value);
        this.value = value;
    }

    private void validateNull(String value) {
        if (value == null) {
            throw new InvalidParameterException(ERROR_MESSAGE_NAME_IS_NOT_NULL);
        }
    }

    public static Name from(String value) {
        return new Name(value);
    }

    public String value() {
        return value;
    }
}
