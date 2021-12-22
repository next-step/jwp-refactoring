package kitchenpos.tobe.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private static final int MIN_QUANTITY = 0;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    protected Quantity() {
    }

    public Quantity(final long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(final long quantity) {
        if (quantity <= MIN_QUANTITY) {
            throw new IllegalArgumentException("수량은 " + MIN_QUANTITY + "보다 커야 합니다.");
        }
    }

    public long asLong() {
        return quantity;
    }

    public BigDecimal asBigDecimal() {
        return BigDecimal.valueOf(quantity);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Quantity quantity1 = (Quantity) o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
