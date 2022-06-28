package kitchenpos.exception;

import static kitchenpos.common.ErrorMessage.INVALID_PRICE;

public class InvalidPriceException extends IllegalArgumentException {

    public InvalidPriceException() {
        super(INVALID_PRICE);
    }

    public InvalidPriceException(String s) {
        super(s);
    }
}
