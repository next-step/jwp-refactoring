package kitchenpos.common.domian;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.error.NegativeValueException;

@Embeddable
public class Price {
    @Column(name = "price")
    private final BigDecimal amount;

    public Price() {
        amount = BigDecimal.valueOf(0);
    }

    public Price(BigDecimal amount) {
        checkNegative(amount);
        this.amount = amount;
    }

    public Price(int amount) {
        this(BigDecimal.valueOf(amount));
    }

    private void checkNegative(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeValueException();
        }
    }

    public Price multiplyQuantity(Quantity quantity) {
        return new Price(amount.multiply(BigDecimal.valueOf(quantity.amount())));
    }

    public int amountToInt() {
        return amount.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return amount.compareTo(price.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
