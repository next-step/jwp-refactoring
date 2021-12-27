package kitchenpos.exception;

import kitchenpos.global.error.ErrorCode;
import kitchenpos.global.exception.BusinessException;

public class TableNotAvailableException extends BusinessException {

    public TableNotAvailableException(final String message) {
        super(message, ErrorCode.TABLE_NOT_AVAILABLE);
    }
}
