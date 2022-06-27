package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    protected MenuProductRequest() {
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
