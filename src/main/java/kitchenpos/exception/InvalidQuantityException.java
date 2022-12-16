package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.*;

public class InvalidQuantityException extends BadRequestException {
    public InvalidQuantityException() {
        super(INVALID_QUANTITY);
    }
}
