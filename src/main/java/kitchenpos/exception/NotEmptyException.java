package kitchenpos.exception;


import static kitchenpos.common.ErrorMessage.NOT_EMPTY_TABLE;

public class NotEmptyException extends IllegalArgumentException {
    public NotEmptyException() {
        super(NOT_EMPTY_TABLE.message());
    }

    public NotEmptyException(String s) {
        super(s);
    }
}
