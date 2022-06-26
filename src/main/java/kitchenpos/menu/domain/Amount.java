package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;

public class Amount {
    private final Price price;
    private final Quantity quantity;

    public Amount(Price price, Quantity quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public int getAmount() {
        return price.getPrice() * quantity.getQuantity();
    }
}
