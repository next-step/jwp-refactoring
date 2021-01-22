package kitchenpos.domain.menu;

import kitchenpos.domain.common.Quantity;

public class ProductQuantity {
    private final Product product;
    private final Quantity quantity;

    public ProductQuantity(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
