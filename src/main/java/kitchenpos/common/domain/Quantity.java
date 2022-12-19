package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {
    private static final String EXCEPTION_MESSAGE_QUANTITY_IS_NOT_NEGATIVE = "수량은 음수일 수 없습니다.";
    private static final long ZERO = 0L;
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long quantity;

    protected Quantity() {
    }

    public Quantity(final Long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public Long value() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity)) return false;
        Quantity quantity1 = (Quantity) o;
        return Objects.equals(quantity, quantity1.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }

    private void validateQuantity(final Long quantity) {
        if (null == quantity || quantity < ZERO) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_QUANTITY_IS_NOT_NEGATIVE);
        }
    }
}
