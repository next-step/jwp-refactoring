package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.EMPTY_TABLE;

public class EmptyTableException extends IllegalStateException {
    public EmptyTableException() {
        super(EMPTY_TABLE);
    }

    public EmptyTableException(String s) {
        super(s);
    }
}
