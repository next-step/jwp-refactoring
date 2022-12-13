package kitchenpos.domain;

import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {

    private long quantity;

    public OrderLineItemQuantity() {
    }

    public OrderLineItemQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}
