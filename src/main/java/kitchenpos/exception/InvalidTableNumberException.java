package kitchenpos.exception;

import static kitchenpos.common.ErrorMessage.INVALID_TABLE_NUMBER;

public class InvalidTableNumberException extends IllegalArgumentException {
    public InvalidTableNumberException() {
        super(INVALID_TABLE_NUMBER.message());
    }

    public InvalidTableNumberException(String s) {
        super(s);
    }
}
