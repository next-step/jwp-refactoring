package kitchenpos.common;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private int quantity;

    protected Quantity() {
    }

    public Quantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
        this.quantity = quantity;
    }

    public long getQuantity() {
        return this.quantity;
    }
}
