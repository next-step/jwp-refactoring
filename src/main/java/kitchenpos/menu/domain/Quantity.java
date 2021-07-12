package kitchenpos.menu.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    private long quantity;

    protected Quantity() {
    }

    public Quantity(final long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(final long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 0 이상입니다.");
        }
    }

    public long value() {
        return quantity;
    }
}
