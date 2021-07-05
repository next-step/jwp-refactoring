package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private Long productId;
    private long quantity;

    private MenuProductResponse(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getProductId(), menuProduct.getQuantity());
    }
}
