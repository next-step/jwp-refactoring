package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    public static final long MIN = 0;

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

    private static void validateQuantity(long quantity) {
        if (quantity < MIN) {
            throw new IllegalArgumentException("수량은 " + MIN + "미만일 수 없습니다.");
        }
    }

    public long value() {
        return this.quantity;
    }
}

