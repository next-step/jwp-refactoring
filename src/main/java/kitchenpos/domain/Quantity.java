package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.InvalidParameterException;

@Embeddable
public class Quantity {
    private static final String ERROR_MESSAGE_QUANTITY_NON_NEGATIVE = "수량은 0 이상이어야 합니다.";

    @Column(name = "quantity", nullable = false)
    private long value;

    protected Quantity() {}

    private Quantity(long value) {
        validateNonNegative(value);
        this.value = value;
    }

    private void validateNonNegative(long value) {
        if (value < 0) {
            throw new InvalidParameterException(ERROR_MESSAGE_QUANTITY_NON_NEGATIVE);
        }
    }

    public static Quantity from(long value) {
        return new Quantity(value);
    }

    public long value() {
        return value;
    }
}
