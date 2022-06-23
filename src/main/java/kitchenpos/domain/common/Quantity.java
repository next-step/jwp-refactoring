package kitchenpos.domain.common;

import java.util.Objects;

public class Quantity {

    private long quantity;

    public Quantity() {
    }

    public Quantity(long quantity) {
        validationQuantity(quantity);
        this.quantity = quantity;
    }

    private void validationQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 0 이상 이어야 합니다.");
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
        Quantity quantity1 = (Quantity) o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
