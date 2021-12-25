package kitchenpos.common.domain;

import kitchenpos.common.exception.IllegalArgumentException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {
    private static final long MINIMUM_QUANTITY = 0;

    @Column
    private Long quantity;

    protected Quantity() {}

    public Quantity(long quantity) {
        checkValidation(quantity);
        this.quantity = quantity;
    }

    public static Quantity of(long quantity) {
        return new Quantity(quantity);
    }

    private void checkValidation(long quantity) {
        if (quantity < MINIMUM_QUANTITY) {
            throw new IllegalArgumentException("유효한 수량은 " + MINIMUM_QUANTITY + "이상 입니다.");
        }
    }

    public Long get() {
        return quantity;
    }
}
