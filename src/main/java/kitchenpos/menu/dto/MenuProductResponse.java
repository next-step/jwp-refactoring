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

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }
}
