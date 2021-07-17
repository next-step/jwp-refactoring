package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(MenuProduct menuProduct) {
        this.seq = menuProduct.getSeq();
        this.menuId = menuProduct.getMenuId();
        this.productId = menuProduct.getProductId();
        this.quantity = quantity;
    }

    public MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
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
