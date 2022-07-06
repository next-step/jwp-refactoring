package kitchenpos.common.exception;


import static kitchenpos.common.message.ErrorMessage.*;

public class NotEmptyException extends IllegalArgumentException {
    public NotEmptyException() {
        super(NOT_EMPTY_TABLE.message());
    }

    public NotEmptyException(String s) {
        super(s);
    }
}
