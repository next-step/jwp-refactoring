package kitchenpos.menu.domain;

import java.util.List;

import kitchenpos.common.domian.Price;
import kitchenpos.common.error.InvalidRequestException;

public class ProductsQuantities {
    private final Products products;
    private final Quantities quantities;
    private final Price totalPrice;

    public ProductsQuantities(Products products, Quantities quantities, Price requestPrice) {
        this.products = products;
        this.quantities = quantities;
        this.totalPrice = products.totalPrice(quantities);
        checkTotalPrice(requestPrice);
    }

    public Price totalPrice() {
        return products.totalPrice(quantities);
    }

    public List<MenuProduct> toMenuProduct(Menu menu) {
        return products.toMenuProducts(menu, quantities);
    }

    private void checkTotalPrice(Price price) {
        if (!this.totalPrice.equals(price)) {
            throw new InvalidRequestException();
        }
    }
}
