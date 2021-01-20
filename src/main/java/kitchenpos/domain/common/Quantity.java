package kitchenpos.domain.common;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

public class Quantity {

    private long quantity;

    protected Quantity() {}

    public Quantity(long quantity) {
        checkArgument(quantity >= 0, "quantity not allowed negative");
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }

    public static Quantity of(long quantity) {
        return new Quantity(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity1 = (Quantity) o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
