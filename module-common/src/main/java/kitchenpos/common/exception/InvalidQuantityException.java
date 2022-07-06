package kitchenpos.common.exception;

import static kitchenpos.common.message.ErrorMessage.*;

public class InvalidQuantityException extends IllegalArgumentException {
    public InvalidQuantityException() {
        super(INVALID_QUANTITY.message());
    }

    public InvalidQuantityException(String s) {
        super(s);
    }
}
