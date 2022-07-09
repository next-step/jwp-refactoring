package kitchenpos.global.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.util.ObjectUtils;

public class Amount {
    private static final int COMPARE_EQUAL_NUMBER = 0;

    private final BigDecimal amount;

    private Amount(BigDecimal amount) {
        validate(amount);
        this.amount = amount;
    }

    private Amount(Price price, Quantity quantity) {
        validateAmount(price, quantity);
        amount = price.multiply(quantity);
    }


    public static Amount from(int amount) {
        return new Amount(BigDecimal.valueOf(amount));
    }

    public static Amount from(BigDecimal amount) {
        return new Amount(amount);
    }

    public static Amount of(Price price, int quantity) {
        return new Amount(price, Quantity.from(quantity));
    }

    public static Amount createSumAmounts(List<Amount> Amounts) {
        return new Amount(Amounts.stream()
                .map(Amount::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    public BigDecimal value() {
        return amount;
    }

    private void validate(BigDecimal amount) {
        if (ObjectUtils.isEmpty(amount) || isZeroEqualOrOver(amount)) {
            throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
        }

    }

    private boolean isZeroEqualOrOver(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) <= COMPARE_EQUAL_NUMBER;
    }

    private void validateAmount(Price price, Quantity quantity) {
        if (ObjectUtils.isEmpty(price)) {
            throw new IllegalArgumentException("가격은 필수 입니다.");
        }
        if (ObjectUtils.isEmpty(quantity)) {
            throw new IllegalArgumentException("수량은 필수 입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Amount amount1 = (Amount) o;
        return Objects.equals(amount, amount1.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
