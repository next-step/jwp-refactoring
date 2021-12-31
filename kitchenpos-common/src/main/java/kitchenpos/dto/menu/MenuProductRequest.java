package kitchenpos.dto.menu;

import kitchenpos.menu.MenuProduct;

public class MenuProductRequest {

    private Long productId;

    private Long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
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
