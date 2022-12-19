package kitchenpos.order.exception;

import kitchenpos.exception.BadRequestException;
import kitchenpos.exception.ErrorMessage;

public class CannotChangeOrderStatusException extends BadRequestException {
    public CannotChangeOrderStatusException() {
        super(ErrorMessage.CANNOT_CHANGE_STATUS);
    }
}
