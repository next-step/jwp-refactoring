package kitchenpos.common;

import kitchenpos.ExceptionMessage;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private long quantity;

    protected Quantity() {}

    public Quantity(long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(ExceptionMessage.QUANTITY_UNDER_ZERO.getMessage());
        }
        this.quantity = quantity;
    }

    public long getValue() {
        return quantity;
    }
}
