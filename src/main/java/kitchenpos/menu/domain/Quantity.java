package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.menu.exception.InvalidQuantityException;

@Embeddable
public class Quantity {
    private Long quantity;

    public Quantity() {
    }

    public Quantity(Long quantity) {
        if (Objects.isNull(quantity) || quantity < 1) {
            throw new InvalidQuantityException();
        }
        this.quantity = quantity;
    }

    public Long value() {
        return quantity;
    }
}
