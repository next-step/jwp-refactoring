package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductRequest {

    private final Long productId;
    private final long quantity;

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity(Product product) {
        return new MenuProduct(product, quantity);
    }

    @Override
    public String toString() {
        return "MenuProductRequest{" +
            "productId=" + productId +
            ", quantity=" + quantity +
            '}';
    }
}
