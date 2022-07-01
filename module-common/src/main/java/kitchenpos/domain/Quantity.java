package kitchenpos.domain;

import java.util.Objects;
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

    public static Quantity from(long quantity) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quantity quantity1 = (Quantity) o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
