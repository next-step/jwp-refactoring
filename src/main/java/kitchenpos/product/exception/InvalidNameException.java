package kitchenpos.product.exception;

import static kitchenpos.exception.ErrorMessage.*;

import kitchenpos.exception.BadRequestException;

public class InvalidNameException extends BadRequestException {
    public InvalidNameException() {
        super(INVALID_NAME);
    }
}
