package kitchenpos.common.domain;

import java.util.Objects;
import kitchenpos.common.exception.NotEnoughMiniumQuantityException;

public class Quantity {

    private static final int MIN = 1;

    private Long value;

    protected Quantity() {
    }

    public Quantity(int value) {
        this((long) value);
    }

    public Quantity(Long value) {
        validationQuantity(value);
        this.value = value;
    }

    private void validationQuantity(Long value) {
        if (value < MIN) {
            throw new NotEnoughMiniumQuantityException(MIN);
        }
    }

    public Long getValue() {
        return value;
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
        return Objects.equals(value, quantity.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
