package kitchenpos.common.exception;

import static kitchenpos.common.message.ErrorMessage.*;

public class EmptyTableException extends IllegalStateException {
    public EmptyTableException() {
        super(EMPTY_TABLE.message());
    }

    public EmptyTableException(String s) {
        super(s);
    }
}
