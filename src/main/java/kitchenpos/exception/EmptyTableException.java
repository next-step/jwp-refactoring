package kitchenpos.exception;

import static kitchenpos.common.ErrorMessage.EMPTY_TABLE;

public class EmptyTableException extends IllegalStateException {
    public EmptyTableException() {
        super(EMPTY_TABLE.message());
    }

    public EmptyTableException(String s) {
        super(s);
    }
}
