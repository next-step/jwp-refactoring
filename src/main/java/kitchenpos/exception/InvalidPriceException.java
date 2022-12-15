package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.*;

public class InvalidPriceException extends BadRequestException {
    public InvalidPriceException() {
        super(INVALID_PRICE);
    }
}
