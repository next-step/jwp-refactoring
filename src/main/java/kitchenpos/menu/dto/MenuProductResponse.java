package kitchenpos.menu.dto;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    private long seq;
    private long menuId;
    private long productId;
    private long quantity;

    protected MenuProductResponse() {
    }

    public MenuProductResponse(long seq, Menu menu, long productId, Quantity quantity) {
        this.seq = seq;
        this.menuId = menu.getId();
        this.productId = productId;
        this.quantity = quantity.value();
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(),
                menuProduct.getMenu(),
                menuProduct.getProductId(),
                menuProduct.getQuantity());
    }

    public long getSeq() {
        return seq;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
