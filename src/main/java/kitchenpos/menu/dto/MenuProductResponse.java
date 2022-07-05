package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private Long productId;
    private Long quantity;

    public MenuProductResponse() {
    }

    private MenuProductResponse(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
