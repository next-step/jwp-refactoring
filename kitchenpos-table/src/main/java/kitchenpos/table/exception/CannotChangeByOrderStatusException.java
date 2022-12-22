package kitchenpos.table.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

import kitchenpos.common.exception.BadRequestException;

public class CannotChangeByOrderStatusException extends BadRequestException {

    public CannotChangeByOrderStatusException() {
        super(CANNOT_CHANGE_BY_ORDER_STATUS);
    }
}
