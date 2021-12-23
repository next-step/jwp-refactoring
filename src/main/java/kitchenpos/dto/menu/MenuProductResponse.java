package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductResponse {

    private Long productId;
    private Long quantity;

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
