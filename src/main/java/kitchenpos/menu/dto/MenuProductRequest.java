package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.order.domain.Quantity;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    protected MenuProductRequest() {
    }

    public MenuProduct toMenuProduct(Product product) {
        return new MenuProduct(product, Quantity.from(quantity));
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
