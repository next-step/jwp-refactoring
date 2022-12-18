package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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

    private void validateQuantity(final Long quantity) {
        if (null == quantity || quantity < ZERO) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_QUANTITY_IS_NOT_NEGATIVE);
        }
    }

    public Long value() {
        return quantity;
    }
}
