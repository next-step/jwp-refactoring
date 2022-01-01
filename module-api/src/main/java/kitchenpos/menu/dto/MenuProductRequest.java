package kitchenpos.menu.dto;

import kitchenpos.moduledomain.menu.MenuProduct;

public class MenuProductRequest {

    private Long productId;
    private Long quantity;

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public MenuProduct toMenuProduct() {
        return MenuProduct.of(productId, quantity);
    }
}
