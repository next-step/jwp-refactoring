package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductResponse() {}

    public MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getId(),
                menuProduct.getMenuId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity());
    }

}
