package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.menu.exception.NegativeQuantityException;

@Embeddable
public class Quantity {
    private static final long MIN_QUANTITY = 0;

    @Column(name = "quantity", nullable = false)
    private long value;

    protected Quantity() {}

    private Quantity(long quantity) {
        this.value = quantity;
    }

    public long getValue() {
        return this.value;
    }

    public static Quantity from(long quantity) {
        validateQuantity(quantity);
        return new Quantity(quantity);
    }

    private static void validateQuantity(long quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new NegativeQuantityException(quantity);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quantity quantity = (Quantity) o;
        return getValue() == quantity.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
