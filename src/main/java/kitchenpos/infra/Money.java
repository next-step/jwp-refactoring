package kitchenpos.infra;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {
    public static final Money ZERO_MONEY = ofZero();

    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        checkPriceGraterThanZero(amount);
        this.amount = amount;
    }

    private static Money ofZero() {
        return new Money(BigDecimal.ZERO);
    }

    public static Money price(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public Long longValue() {
        return amount.longValue();
    }

    public Integer intValue() {
        return amount.intValue();
    }

    private void checkPriceGraterThanZero(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품의 가격은 0보다 작을수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount.longValue(), money.amount.longValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    public Money multiply(long quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }

    public Money add(Money money) {
        return new Money(this.amount.add(money.amount));
    }

    public static Money sum(Money augend, Money added) {
        return augend.add(added);
    }

    public boolean isGraterThan(Money price) {
        return this.amount.compareTo(price.amount) > 0;
    }
}
