package kitchenpos.common;

import java.util.Objects;

public class Quantity {
    private final Long quantity;

    public Quantity() {
        this.quantity = 0L;
    }

    public Quantity(Long quantity) {
        checkNegative(quantity);
        this.quantity = quantity;
    }

    public Long quantity() {
        return quantity;
    }

    private void checkNegative(Long quantity) {
        if (quantity < 0L) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return Objects.equals(quantity, quantity.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
