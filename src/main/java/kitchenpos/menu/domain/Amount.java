package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductPrice;

public class Amount {
    private final ProductPrice price;
    private final int quantity;

    public Amount(ProductPrice price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public int calculateAmount() {
        return price.calculateAmount(quantity);
    }
}
