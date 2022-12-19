package kitchenpos.common.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private static final long MIN = 0;

    @Column(nullable = false)
    private long quantity;

    protected Quantity() {}

    public Quantity(long quantity) {
        validateNotUnderValue(quantity);
        this.quantity = quantity;
    }

    public static Quantity from(long quantity) {
        return new Quantity(quantity);
    }

    private static void validateNotUnderValue(long quantity) {
        if (quantity < MIN) {
            throw new IllegalArgumentException("수량은 " + MIN + "개 이상이어야 합니다.");
        }
    }

    public long value() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quantity)) {
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
