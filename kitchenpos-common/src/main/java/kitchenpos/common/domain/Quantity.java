package kitchenpos.common.domain;

import kitchenpos.common.exception.InvalidQuantityException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private static final String EXCEPTION_MESSAGE_MIN_QUANTITY = "수량은 %s보다 작을수 없습니다.";
    private static final int MIN_QUANTITY = 0;

    @Column(name = "quantity")
    private long quantity;

    public Quantity() {
    }

    public Quantity(long quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new InvalidQuantityException(EXCEPTION_MESSAGE_MIN_QUANTITY);
        }
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}
