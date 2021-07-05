package kitchenpos.menu.domain;

import java.util.List;

import kitchenpos.common.domian.Price;
import kitchenpos.menuproduct.domain.MenuProduct;

public class ProductsQuantities {
    private final Products products;
    private final Quantities quantities;

    public ProductsQuantities(Products products, Quantities quantities) {
        this.products = products;
        this.quantities = quantities;
    }

    public Price totalPrice() {
        return products.totalPrice(quantities);
    }

    public List<MenuProduct> toMenuProduct() {
        return products.toMenuProducts(quantities);
    }
}
