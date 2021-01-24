package kitchenpos.common;

import java.util.Objects;

public class Quantity {

    private long quantity;

    protected Quantity() {}

    public Quantity(long quantity) {
        checkQuantityArgument(quantity);
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }

    public static Quantity of(long quantity) {
        return new Quantity(quantity);
    }

    private void checkQuantityArgument(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity not allowed negative");
        }
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
