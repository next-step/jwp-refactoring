package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.INVALID_QUANTITY;

public class InvalidQuantityException extends IllegalArgumentException {
    public InvalidQuantityException() {
        super(INVALID_QUANTITY);
    }

    public InvalidQuantityException(String s) {
        super(s);
    }
}
