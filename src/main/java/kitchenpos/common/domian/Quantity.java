package kitchenpos.common.domian;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.error.CustomException;
import kitchenpos.error.ErrorInfo;

@Embeddable
public class Quantity {
    @Column(name = "quantity")
    private final Long amount;

    public Quantity() {
        this.amount = 0L;
    }

    public Quantity(Long amount) {
        checkNegative(amount);
        this.amount = amount;
    }

    public Long amount() {
        return amount;
    }

    private void checkNegative(Long amount) {
        if (amount < 0L) {
            throw new CustomException(ErrorInfo.QUANTITY_CAN_NOT_NEGATIVE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return Objects.equals(amount, quantity.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
