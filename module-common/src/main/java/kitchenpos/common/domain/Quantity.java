package kitchenpos.common.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.InvalidQuantityException;

@Embeddable
public class Quantity {
    private static final Long MIN_QUANTITY = 1L;

    @Column(nullable = false)
    private Long quantity;

    protected Quantity() {
    }

    private Quantity(Long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public static Quantity of(Long quantity) {
        return new Quantity(quantity);
    }

    private void validateQuantity(Long quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new InvalidQuantityException();
        }
    }

    public Long value() {
        return quantity;
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
        return Objects.equals(quantity, quantity1.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
