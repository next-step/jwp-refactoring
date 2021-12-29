package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

import java.util.Objects;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu().getId(), menuProduct.getProduct().getId(), menuProduct.getQuantity());
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
