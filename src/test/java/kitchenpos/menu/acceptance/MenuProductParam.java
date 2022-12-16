package kitchenpos.menu.acceptance;

import kitchenpos.product.acceptance.ProductId;

public class MenuProductParam {
    private final long productId;
    private final long quantity;

    public MenuProductParam(ProductId productId, long quantity) {
        this.productId = productId.value();
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
