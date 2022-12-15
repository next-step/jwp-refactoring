package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.*;

public class InvalidMenuPriceException extends BadRequestException {
    public InvalidMenuPriceException() {
        super(INVALID_SUM_OF_PRICE);
    }
}
