package kitchenpos.common.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

public class NotExistIdException extends BadRequestException {
    public NotExistIdException() {
        super(NOT_EXIST_ID);
    }
}
