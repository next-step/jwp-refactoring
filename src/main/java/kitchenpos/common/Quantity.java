package kitchenpos.common;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Quantity {
    private static final int ZERO = 0;
    private long quantity;

    protected Quantity() {
    }

    public Quantity(long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(long quantity) {
        if (quantity <= ZERO) {
            throw new IllegalArgumentException("수량은 0 이거나 음수 일 수 없습니다.");
        }
    }

    public long value() {
        return quantity;
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(quantity);
    }
}
