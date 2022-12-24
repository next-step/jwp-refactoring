package kitchenpos.acceptance.menu;

import kitchenpos.acceptance.product.ProductId;

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
