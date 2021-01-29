package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

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

    public MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct, Long menuId, Long productId) {
        return new MenuProductResponse(menuProduct.getSeq(),
            menuId,
            productId,
            menuProduct.getQuantity());
    }

    @Override
    public String toString() {
        return "MenuProductResponse{" +
            "seq=" + seq +
            ", menuId=" + menuId +
            ", productId=" + productId +
            ", quantity=" + quantity +
            '}';
    }
}
