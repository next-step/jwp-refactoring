package kitchenpos.infra;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        checkPriceGraterThanZero(amount);
        this.amount = amount;
    }

    public static Money price(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public Long longValue() {
        return amount.longValue();
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

    public Money add(Money money) {
        return new Money(this.amount.add(money.amount));
    }
}
