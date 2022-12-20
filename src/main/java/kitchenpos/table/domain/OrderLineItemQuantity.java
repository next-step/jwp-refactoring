package kitchenpos.table.domain;

import kitchenpos.exception.ErrorMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {
    @Column
    private long quantity;

    protected OrderLineItemQuantity() {}

    public OrderLineItemQuantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if(isNegativeNumber(quantity)) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_LINE_ITEM_INVALID_QUANTITY.getMessage());
        }
    }

    private static boolean isNegativeNumber(long quantity) {
        return quantity < 0;
    }

    public long value() {
        return quantity;
    }
}
