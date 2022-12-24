package kitchenpos.common.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

public class InvalidQuantityException extends BadRequestException {
    public InvalidQuantityException() {
        super(INVALID_QUANTITY);
    }
}
