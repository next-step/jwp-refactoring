package kitchenpos.common.domain;

import kitchenpos.common.constant.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    public static final Long ZERO = 0L;

    @Column(nullable = false)
    private Long quantity;

    protected Quantity() {}

    public Quantity(Long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(Long quantity) {
        if (quantity < ZERO) {
            throw new IllegalArgumentException(ErrorCode.QUANTITY_SHOULD_OVER_ZERO.getMessage());
        }
    }

    public Long value() {
        return quantity;
    }
}
