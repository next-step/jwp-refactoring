package kitchenpos.table.exception;

import static kitchenpos.exception.ErrorMessage.*;

import kitchenpos.exception.BadRequestException;

public class NotEmptyTableException extends BadRequestException {

    public NotEmptyTableException() {
        super(NOT_EMPTY_TABLE);
    }
}
