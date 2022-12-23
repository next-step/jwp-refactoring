package common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

import static common.ErrorMessage.VALIDATION_OF_QUANTITY;

@Embeddable
public class Quantity {

    private static final long ZERO = 0;

    @Column(nullable = false)
    private long quantity;

    protected Quantity() {
    }

    private Quantity(long quantity) {
        validateQuantityIsSmallerThanZero(quantity);
        this.quantity = quantity;
    }

    public static Quantity from(long quantity) {
        return new Quantity(quantity);
    }

    private void validateQuantityIsSmallerThanZero(long quantity) {
        if (quantity < ZERO) {
            throw new IllegalArgumentException(VALIDATION_OF_QUANTITY.getMessage());
        }
    }

    public long value() {
        return quantity;
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(quantity);
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
