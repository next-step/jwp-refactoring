package kitchenpos.order.exception;

import static kitchenpos.exception.ErrorMessage.*;

import kitchenpos.exception.BadRequestException;

public class AtLeastOneOrderLineItemException extends BadRequestException {

    public AtLeastOneOrderLineItemException() {
        super(ORDER_LINE_ITEM_COUNT);
    }
}
