package kitchenpos.product.exception;

import static kitchenpos.exception.ErrorMessage.*;

import kitchenpos.exception.BadRequestException;

public class InvalidPriceException extends BadRequestException {
    public InvalidPriceException() {
        super(INVALID_PRICE);
    }
}
