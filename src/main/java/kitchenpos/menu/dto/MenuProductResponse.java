package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long productId;
    private Long menuId;
    private Long quantity;

    private MenuProductResponse(Long seq, Long productId, Long menuId, Long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.menuId = menuId;
        this.quantity = quantity;
    }
    public static MenuProductResponse of(MenuProduct menuProduct, Menu menu) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getProductId(),
                menu.getId(),
                menuProduct.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
