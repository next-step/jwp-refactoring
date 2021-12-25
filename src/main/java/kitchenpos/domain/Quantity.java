package kitchenpos.domain;

import kitchenpos.exception.InvalidQuantityException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    private static final Long QUANTITY_MIN_VALUE = 1L;
    private static final String INVALID_QUANTITY_EXCEPTION = "수량은 최소 1개입니다.";

    @Column(name = "quantity")
    private Long quantity;

    protected Quantity() {

    }

    private Quantity(Long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    public static Quantity of(Long quantity) {
        return new Quantity(quantity);
    }

    private void validate(Long quantity) {
        if (quantity < QUANTITY_MIN_VALUE) {
            throw new InvalidQuantityException(INVALID_QUANTITY_EXCEPTION);
        }
    }

    public Long getQuantity() {
        return quantity;
    }
}
