package kitchenpos.domain.common;

import java.math.BigDecimal;
import java.util.Objects;

public class Amount {

    private BigDecimal amount;

    public Amount() {
    }

    public Amount(BigDecimal amount) {
        validationAmount(amount);
        this.amount = amount;
    }

    private void validationAmount(BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액은 0원 이상이어야 합니다.");
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
