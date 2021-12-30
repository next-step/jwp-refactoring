package kitchenpos.common.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.exception.BadRequestException;

@Embeddable
public class Quantity {

    public static final int QUANTITY_MIN = 0;

    @Column
    private long quantity;

    protected Quantity() {
    }

    public Quantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if (quantity < QUANTITY_MIN) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    public long getValue() {
        return quantity;
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
