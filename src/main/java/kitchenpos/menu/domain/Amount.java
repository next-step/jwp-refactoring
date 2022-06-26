package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

public class Amount {
    private final Price price;
    private final MenuProductQuantity quantity;

    public Amount(Price price, MenuProductQuantity quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public int getAmount() {
        return price.getPrice() * quantity.getQuantity();
    }
}
