package kitchenpos.exception;

import static kitchenpos.exception.ErrorMessage.*;

public class AtLeastOneOrderLineItemException extends BadRequestException {

    public AtLeastOneOrderLineItemException() {
        super(ORDER_LINE_ITEM_COUNT);
    }
}
