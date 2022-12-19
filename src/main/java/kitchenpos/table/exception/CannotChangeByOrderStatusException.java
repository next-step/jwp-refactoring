package kitchenpos.table.exception;

import kitchenpos.exception.BadRequestException;
import kitchenpos.exception.ErrorMessage;

public class CannotChangeByOrderStatusException extends BadRequestException {

    public CannotChangeByOrderStatusException() {
        super(ErrorMessage.CANNOT_CHANGE_BY_ORDER_STATUS);
    }
}
