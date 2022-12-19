package kitchenpos.common;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private int quantity;

    protected Quantity() {
    }

    public Quantity(int quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private static void validate(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
    }

    public long getQuantity() {
        return this.quantity;
    }
}
