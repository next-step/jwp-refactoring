package kitchenpos.table.exception;

import kitchenpos.global.error.ErrorCode;
import kitchenpos.global.exception.BusinessException;

public class CannotChangeTableEmptyException extends BusinessException {

    public CannotChangeTableEmptyException(final String message) {
        super(message, ErrorCode.ALREADY_TABLE_GROUP);
    }
}
