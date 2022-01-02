package kitchenpos.tablegroup.exception;

import kitchenpos.global.error.ErrorCode;
import kitchenpos.global.exception.BusinessException;

public class TableGroupNotAvailableException extends BusinessException {

    public TableGroupNotAvailableException(final String message) {
        super(message, ErrorCode.TABLE_GROUP_NOT_AVAILABLE);
    }
}
