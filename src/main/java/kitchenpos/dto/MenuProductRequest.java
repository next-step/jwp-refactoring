package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {

    private final Long productId;
    private final long quantity;

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest from(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
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
