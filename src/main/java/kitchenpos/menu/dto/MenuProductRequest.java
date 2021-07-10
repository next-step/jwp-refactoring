package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProductEntity;
import kitchenpos.product.domain.ProductEntity;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    protected MenuProductRequest() {
    }

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

    public MenuProductEntity toEntity(ProductEntity productEntity) {
        return new MenuProductEntity(productEntity, quantity);
    }
}
