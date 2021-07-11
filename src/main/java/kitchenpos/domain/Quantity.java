package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    private long quantity;

    public static Quantity valueOf(long quantity) {
        return new Quantity(quantity);
    }

    public Quantity() {
    }

    public Quantity(long quantity) {
        this.quantity = quantity;
    }

    public long value() {
        return quantity;
    }

    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Quantity quantity1 = (Quantity)o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
