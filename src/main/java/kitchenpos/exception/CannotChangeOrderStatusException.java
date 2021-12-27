package kitchenpos.exception;

import kitchenpos.global.error.ErrorCode;
import kitchenpos.global.exception.BusinessException;

public class CannotChangeOrderStatusException extends BusinessException {

    public CannotChangeOrderStatusException(final String message) {
        super(message, ErrorCode.ORDER_STATUS_COMPLETION);
    }
}
