package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    protected MenuProductRequest() {}

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
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
