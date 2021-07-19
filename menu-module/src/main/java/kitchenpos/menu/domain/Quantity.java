package kitchenpos.menu.domain;

import kitchenpos.exception.IllegalQuantityException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {

    public static final String ILLEGAL_QUANTITY_ERROR_MESSAGE = "수량은 1이상의 값만 입력 가능합니다.";
    public static final int MINIMUM_QUANTITY = 1;
    private long quantity;

    public Quantity() {
    }

    public Quantity(long quantity) {
        checkValidQuantity(quantity);
        this.quantity = quantity;
    }

    public long quantity() {
        return quantity;
    }

    private void checkValidQuantity(long quantity) {
        if (quantity < MINIMUM_QUANTITY) {
            throw new IllegalQuantityException(ILLEGAL_QUANTITY_ERROR_MESSAGE);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Quantity quantity1 = (Quantity) object;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
