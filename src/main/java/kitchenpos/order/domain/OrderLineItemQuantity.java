package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderLineItemExceptionCode;
import kitchenpos.utils.NumberUtil;

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
        if(NumberUtil.isNotPositiveNumber(quantity)) {
            throw new IllegalArgumentException(OrderLineItemExceptionCode.INVALID_QUANTITY.getMessage());
        }
    }

    public long getQuantity() {
        return quantity;
    }
}
