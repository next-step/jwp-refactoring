package kitchenpos.dto;

import kitchenpos.domain.MenuProductEntity;
import kitchenpos.domain.ProductEntity;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    protected MenuProductRequest() {
    }

    public MenuProductEntity toMenuProduct(ProductEntity product) {
        return new MenuProductEntity(product, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

}
