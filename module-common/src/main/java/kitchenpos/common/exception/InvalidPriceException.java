package kitchenpos.common.exception;

import static kitchenpos.common.message.ErrorMessage.*;

public class InvalidPriceException extends IllegalArgumentException {

    public InvalidPriceException() {
        super(INVALID_PRICE.message());
    }

    public InvalidPriceException(String s) {
        super(s);
    }
}
