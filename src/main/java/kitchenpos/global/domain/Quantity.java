package kitchenpos.global.domain;

import kitchenpos.menu.message.QuantityMessage;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(nullable = false)
    private final Long quantity;

    protected Quantity() {
        this.quantity = null;
    }

    private Quantity(Long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public static Quantity of(Long quantity) {
        return new Quantity(quantity);
    }

    private void validateQuantity(Long quantity) {
        if(quantity == null) {
            throw new IllegalArgumentException(QuantityMessage.CREATE_ERROR_QTY_MUST_BE_NOT_NULL.message());
        }

        if(quantity < 0L) {
            throw new IllegalArgumentException(QuantityMessage.CREATE_ERROR_QTY_MUST_BE_GREATER_THAN_ZERO.message());
        }
    }

    public Long value() {
        return this.quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quantity quantity1 = (Quantity) o;

        return quantity.equals(quantity1.quantity);
    }

    @Override
    public int hashCode() {
        return quantity.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(quantity);
    }
}
