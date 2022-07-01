package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static kitchenpos.common.Messages.QUANTITY_CANNOT_ZERO_LESS_THAN;

@Embeddable
public class Quantity {

    @Column(nullable = false)
    private long quantity;

    protected Quantity() {
    }

    private Quantity(long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public static Quantity of(long quantity) {
        return new Quantity(quantity);
    }

    private void validateQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(QUANTITY_CANNOT_ZERO_LESS_THAN);
        }
    }

    public long getQuantity() {
        return quantity;
    }
}
