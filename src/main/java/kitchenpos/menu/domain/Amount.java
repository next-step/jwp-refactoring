package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductPrice;

public class Amount {
    private final ProductPrice price;
    private final MenuProductQuantity quantity;

    public Amount(ProductPrice price, MenuProductQuantity quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public int getAmount() {
        return price.getPrice() * quantity.getQuantity();
    }
}
