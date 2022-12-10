package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    private MenuProductRequest() {}

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct(Product product) {
        return MenuProduct.of(product, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
