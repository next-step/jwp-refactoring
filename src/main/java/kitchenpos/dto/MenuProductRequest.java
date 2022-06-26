package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {
    private Long productId;
    private int quantity;

    private MenuProductRequest() {
    }

    private MenuProductRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(Long productId, int quantity) {
        return new MenuProductRequest(productId, quantity);
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
