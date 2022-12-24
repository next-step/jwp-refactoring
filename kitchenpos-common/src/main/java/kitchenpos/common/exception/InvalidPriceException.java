package kitchenpos.common.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

public class InvalidPriceException extends BadRequestException {
    public InvalidPriceException() {
        super(INVALID_PRICE);
    }
}
