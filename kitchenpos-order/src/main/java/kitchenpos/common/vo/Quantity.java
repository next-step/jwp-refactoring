package kitchenpos.common.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.common.exception.ErrorMessage;

@Embeddable
public class Quantity {
    public static String PROPERTY_NAME = "수량";
    @Column(name = "quantity", nullable = false)
    private int value;

    protected Quantity() {}

    private Quantity(int quantity) {
        validateQuantityIsNegative(quantity);
        this.value = quantity;
    }

    private void validateQuantityIsNegative(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(ErrorMessage.cannotBeNull(PROPERTY_NAME));
        }
    }

    public static Quantity of(int quantity) {
        return new Quantity(quantity);
    }

    public int value() {
        return value;
    }
}
