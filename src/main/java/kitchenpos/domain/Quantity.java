package kitchenpos.domain;

import java.math.BigDecimal;

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
}
