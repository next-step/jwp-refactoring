package kitchenpos.common.domain;

import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {
    private static final int MIN_NAME_LENGTH = 0;

    @Column
    private String name;

    protected Name() {

    }

    public Name(String name) {
        validate(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void validate(String name) {
        if (isEmpty(name)) {
            throw new InputDataException(InputDataErrorCode.THE_NAME_CAN_NOT_EMPTY);
        }
    }

    private boolean isEmpty(String name) {
        return name == null || name.length() == MIN_NAME_LENGTH;
    }
}
