package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.menu.exception.InvalidQuantityException;

@Embeddable
public class ItemQuantity {
    private Long quantity;

    public ItemQuantity() {
    }

    public ItemQuantity(Long quantity) {
        if (Objects.isNull(quantity) || quantity < 1) {
            throw new InvalidQuantityException();
        }
        this.quantity = quantity;
    }

    public Long value() {
        return quantity;
    }
}
