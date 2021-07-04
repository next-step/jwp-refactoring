package kitchenpos.common.domian;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.error.ErrorInfo;
import kitchenpos.error.CustomException;

@Embeddable
public class Price {
    @Column(name = "price")
    private final BigDecimal amount;

    public Price() {
        amount = BigDecimal.valueOf(0);
    }

    public Price(int amount) {
        checkNegative(amount);
        this.amount = BigDecimal.valueOf(amount);
    }

    public Price(BigDecimal amount) {
        checkNegative(amount.intValue());
        this.amount = amount;
    }

    private void checkNegative(int amount) {
        if (amount < 0) {
            throw new CustomException(ErrorInfo.PRICE_CAN_NOT_NEGATIVE);
        }
    }

    public Price sum(Price price, Quantity quantity) {
        return new Price(price.amount.multiply(BigDecimal.valueOf(quantity.amount())));
    }

    public int amountToInt() {
        return amount.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(amount, price.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
