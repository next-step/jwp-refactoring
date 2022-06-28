package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.NOT_EMPTY_TABLE;

public class NotEmptyException extends IllegalArgumentException {
    public NotEmptyException() {
        super(NOT_EMPTY_TABLE);
    }

    public NotEmptyException(String s) {
        super(s);
    }
}
