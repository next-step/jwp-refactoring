package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.error.ErrorEnum;

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
            throw new IllegalArgumentException(ErrorEnum.QUANTITY_UNDER_ZERO.message());
        }
    }

    public Long value() {
        return quantity;
    }
}
