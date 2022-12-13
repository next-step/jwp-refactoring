package kitchenpos.menu.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private long quantity;

    public Quantity() {}

    public Quantity(long quantity) {
        this.quantity = quantity;
    }

    public long getValue() {
        return quantity;
    }
}
