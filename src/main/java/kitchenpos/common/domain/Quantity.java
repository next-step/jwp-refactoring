package kitchenpos.common.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    private long quantity;

    protected Quantity() {
    }

    public Quantity(long quantity) {
        this.quantity = quantity;
    }

    public long value() {
        return quantity;
    }
}
