package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.*;

public class NotEmptyTableException extends BadRequestException {

    public NotEmptyTableException() {
        super(NOT_EMPTY_TABLE);
    }
}
