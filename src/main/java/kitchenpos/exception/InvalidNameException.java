package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.*;

public class InvalidNameException extends BadRequestException {
    public InvalidNameException() {
        super(INVALID_NAME);
    }
}
