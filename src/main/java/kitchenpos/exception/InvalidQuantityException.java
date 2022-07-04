package kitchenpos.exception;

import static kitchenpos.common.ErrorMessage.INVALID_QUANTITY;


public class InvalidQuantityException extends IllegalArgumentException {
    public InvalidQuantityException() {
        super(INVALID_QUANTITY.message());
    }

    public InvalidQuantityException(String s) {
        super(s);
    }
}
