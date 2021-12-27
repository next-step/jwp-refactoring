package kitchenpos.common.vo;

import java.util.Objects;
import javax.persistence.Embeddable;
import kitchenpos.order.exception.InvalidQuantityValueException;

@Embeddable
public class Quantity {

    private long quantity;

    protected Quantity() {
    }

    public Quantity(long quantity) {
        validateQuantityValue(quantity);
        this.quantity = quantity;
    }

    private void validateQuantityValue(long inputQuantity) {
        if (inputQuantity < 0) {
            throw new InvalidQuantityValueException();
        }
    }

    public long getQuantity() {
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
        return getQuantity() == quantity1.getQuantity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuantity());
    }
}
