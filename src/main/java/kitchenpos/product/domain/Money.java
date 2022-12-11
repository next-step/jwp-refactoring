package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {
    private BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money of(Long amount) {
        validateMoney(amount);
        return new Money(BigDecimal.valueOf(amount));
    }

    private static void validateMoney(Long amount) {
        if (Objects.isNull(amount)) {
            throw new IllegalArgumentException();
        }
        if (BigDecimal.valueOf(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }
}
