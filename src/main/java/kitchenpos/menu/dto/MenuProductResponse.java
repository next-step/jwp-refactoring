package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Quantity;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private Quantity quantity;

    private MenuProductResponse(Long seq, Long menuId, Long productId, Quantity quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(final Quantity quantity) {
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu().getId(), menuProduct.getProductId(), menuProduct.getQuantity());
    }
}
