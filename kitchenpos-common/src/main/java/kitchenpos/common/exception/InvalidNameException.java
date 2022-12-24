package kitchenpos.common.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

public class InvalidNameException extends BadRequestException {
    public InvalidNameException() {
        super(INVALID_NAME);
    }
}
