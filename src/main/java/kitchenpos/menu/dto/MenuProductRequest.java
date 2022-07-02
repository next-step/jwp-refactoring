package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "MenuProductRequest{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
