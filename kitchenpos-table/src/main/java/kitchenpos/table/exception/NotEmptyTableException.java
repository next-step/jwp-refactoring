package kitchenpos.table.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

import kitchenpos.common.exception.BadRequestException;

public class NotEmptyTableException extends BadRequestException {

    public NotEmptyTableException() {
        super(NOT_EMPTY_TABLE);
    }
}
