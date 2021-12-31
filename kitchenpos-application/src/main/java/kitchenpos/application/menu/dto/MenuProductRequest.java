package kitchenpos.application.menu.dto;

import kitchenpos.domain.menu.domain.MenuProduct;

public class MenuProductRequest {
    private final long productId;
    private final long quantity;

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity() {
        return MenuProduct.of(productId, quantity);
    }
}
