package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {
    private Long productId;

    private Long quantity;

    protected MenuProductRequest() {
    }

    private MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public MenuProduct toEntity() {
        return MenuProduct.of(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
