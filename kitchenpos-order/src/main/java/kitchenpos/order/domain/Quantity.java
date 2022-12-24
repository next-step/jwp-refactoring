package kitchenpos.order.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    public static final int QUANTITY_MINIMUM_SIZE = 0;
    private long quantity;

    protected Quantity() {
    }

    public Quantity(int quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private static void validate(int quantity) {
        if (quantity < QUANTITY_MINIMUM_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    public long getQuantity() {
        return this.quantity;
    }
}
