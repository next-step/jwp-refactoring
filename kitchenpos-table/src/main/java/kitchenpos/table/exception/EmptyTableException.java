package kitchenpos.table.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

import kitchenpos.common.exception.BadRequestException;

public class EmptyTableException extends BadRequestException {

    public EmptyTableException() {
        super(EMPTY_TABLE);
    }
}
