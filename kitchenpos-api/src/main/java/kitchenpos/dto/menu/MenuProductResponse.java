package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {
        // empty
    }

    private MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(final MenuProduct menuProduct, final Long menuId) {
        return new MenuProductResponse(menuProduct.getSeq(),
                                       menuId,
                                       menuProduct.getProductId(),
                                       menuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
