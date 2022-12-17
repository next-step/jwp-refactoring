package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.*;

public class NotExistIdException extends BadRequestException {
    public NotExistIdException() {
        super(NOT_EXIST_ID);
    }
}
