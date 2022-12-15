package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.constant.ErrorCode;

@Embeddable
public class Quantity {

    private static final long ZERO = 0;

    @Column(nullable = false)
    private long quantity;

    protected Quantity() {}

    private Quantity(long quantity) {
        validateQuantityLessThanZero(quantity);
        this.quantity = quantity;
    }

    public static Quantity from(long quantity) {
        return new Quantity(quantity);
    }

    private void validateQuantityLessThanZero(long quantity) {
        if (quantity < ZERO) {
            throw new IllegalArgumentException(ErrorCode.QUANTITY_LESS_THAN_ZERO.getErrorMessage());
        }
    }

    public long value() {
        return quantity;
    }
}
