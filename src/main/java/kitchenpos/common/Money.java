package kitchenpos.common;

import java.math.BigDecimal;
import java.util.Objects;

public class Money implements Comparable<Money> {

    private static final BigDecimal MIN_VALUE = BigDecimal.ZERO;

    private final BigDecimal amount;

    public Money(final BigDecimal amount) {
        if (amount.compareTo(MIN_VALUE) < 0) {
            throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
        }
        this.amount = amount;
    }

    public static Money valueOf(int value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public Money add(final Money other) {
        return new Money(amount.add(other.amount));
    }

    public Money subtract(final Money other) {
        return new Money(amount.subtract(other.amount));
    }


    public Money multiply(final long value) {
        return new Money(amount.multiply(BigDecimal.valueOf(value)));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public int compareTo(final Money other) {
        return amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        final Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
