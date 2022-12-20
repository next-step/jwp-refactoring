package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;


public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {
    }

    private MenuProductResponse(final MenuProduct menuProduct) {
        this.seq = menuProduct.getSeq();
        this.menuId = menuProduct.getMenuId();
        this.productId = menuProduct.getProductId();
        this.quantity = menuProduct.getQuantity();
    }

    public static MenuProductResponse of(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct);
    }
}
