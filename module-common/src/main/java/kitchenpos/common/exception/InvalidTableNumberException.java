package kitchenpos.common.exception;

import static kitchenpos.common.message.ErrorMessage.*;

public class InvalidTableNumberException extends IllegalArgumentException {
    public InvalidTableNumberException() {
        super(INVALID_TABLE_NUMBER.message());
    }

    public InvalidTableNumberException(String s) {
        super(s);
    }
}
