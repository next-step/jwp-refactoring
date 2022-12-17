package kitchenpos.core.domain;

import kitchenpos.core.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.util.Objects;
import static java.util.Objects.requireNonNull;

public class Amount implements Comparable<Amount> {

    public static final Amount ZERO = new Amount(BigDecimal.ZERO);

    private BigDecimal amount;

    public Amount(BigDecimal amount) {
        validate(amount);
        this.amount = amount;
    }

    private void validate(BigDecimal amount) {
        requireNonNull(amount, "amount");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException();
        }
    }

    public Amount add(Amount amount) {
        return new Amount(this.amount.add(amount.amount));
    }

    public boolean isGatherThan(Amount other) {
        return this.compareTo(other) > 0;
    }

    public BigDecimal getValue() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount that = (Amount) o;
        return compareTo(that) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public int compareTo(Amount o) {
        return amount.compareTo(o.amount);
    }
}
