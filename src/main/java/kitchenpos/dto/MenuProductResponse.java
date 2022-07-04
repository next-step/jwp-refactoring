package kitchenpos.dto;

import java.util.function.Predicate;
import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {
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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return  new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu().getId(), menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }
}
