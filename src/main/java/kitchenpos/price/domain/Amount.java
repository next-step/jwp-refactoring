package kitchenpos.price.domain;

import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Amount implements Comparable<Amount> {

    public static final Amount ZERO = new Amount(BigDecimal.ZERO);

    private BigDecimal amount;

    public Amount(BigDecimal amount) {
        this.amount = validate(amount);
    }

    private BigDecimal validate(BigDecimal amount) {
        requireNonNull(amount, "amount");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("유효하지 않은 금액입니다.");
        }
        return amount;
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
