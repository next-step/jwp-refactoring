package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.INVALID_TABLE_NUMBER;

public class InvalidTableNumberException extends IllegalArgumentException {
    public InvalidTableNumberException() {
        super(INVALID_TABLE_NUMBER);
    }

    public InvalidTableNumberException(String s) {
        super(s);
    }
}
