package kitchenpos.domain.common;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private BigDecimal amount;

    protected Money() {}

    public Money(BigDecimal amount) {
        checkAmount(amount);
        this.amount = amount;
    }

    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public boolean isGreaterThan(Money money) {
        return this.amount.compareTo(money.getAmount()) > 0;
    }

    public void add(Money money) {
        this.amount = this.amount.add(money.getAmount());
    }

    public Money multiply(long price) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(price)));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    private void checkAmount(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("price was null or negative value");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
