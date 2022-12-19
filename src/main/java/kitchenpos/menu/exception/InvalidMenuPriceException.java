package kitchenpos.menu.exception;

import static kitchenpos.exception.ErrorMessage.*;

import kitchenpos.exception.BadRequestException;

public class InvalidMenuPriceException extends BadRequestException {
    public InvalidMenuPriceException() {
        super(INVALID_SUM_OF_PRICE);
    }
}
