package kitchenpos.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    private static final long MIN_QUANTITY_VALUE = 1L;
    private long quantity;

    protected Quantity() {

    }

    public Quantity(long quantity) {
        valid(quantity);
        this.quantity = quantity;
    }

    public static Quantity of(int quantity) {
        return new Quantity(quantity);
    }

    public static Quantity of(long quantity) {
        return new Quantity(quantity);
    }

    public long value() {
        return quantity;
    }

    private void valid(long quantity) {
        if (quantity < MIN_QUANTITY_VALUE) {
            throw new IllegalArgumentException("수량은 1개 이상 이어야야 합니다.");
        }
    }
}
