package kitchenpos.menu.exception;

import static kitchenpos.exception.ErrorMessage.*;

import kitchenpos.exception.BadRequestException;

public class InvalidQuantityException extends BadRequestException {
    public InvalidQuantityException() {
        super(INVALID_QUANTITY);
    }
}
