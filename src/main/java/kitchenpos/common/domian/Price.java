package kitchenpos.common.domian;

import javax.persistence.Embeddable;

import kitchenpos.error.ErrorInfo;
import kitchenpos.error.CustomException;

import java.util.Objects;

@Embeddable
public class Price {
    private final int amount;

    public Price() {
        amount = 0;
    }

    public Price(int amount) {
        checkNegative(amount);
        this.amount = amount;
    }

    private void checkNegative(int amount) {
        if (amount < 0) {
            throw new CustomException(ErrorInfo.PRICE_CAN_NOT_NEGATIVE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return amount == price.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
