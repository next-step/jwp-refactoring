package kitchenpos.order.exception;

import static kitchenpos.common.constant.ErrorMessage.*;

import kitchenpos.common.exception.BadRequestException;

public class AtLeastOneOrderLineItemException extends BadRequestException {

    public AtLeastOneOrderLineItemException() {
        super(ORDER_LINE_ITEM_COUNT);
    }
}
