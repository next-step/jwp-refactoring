package kitchenpos.order.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

import kitchenpos.common.exception.BadRequestException;

public class CannotChangeOrderStatusException extends BadRequestException {
    public CannotChangeOrderStatusException() {
        super(CANNOT_CHANGE_STATUS);
    }
}
