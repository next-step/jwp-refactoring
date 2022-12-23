package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {
    private Long productId;
    private int quantity;

    public MenuProductRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(productId, quantity);
    }
}
