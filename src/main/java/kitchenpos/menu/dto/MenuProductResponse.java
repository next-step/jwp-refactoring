package kitchenpos.menu.dto;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductResponse {

    private long seq;
    private long menuId;
    private long productId;
    private long quantity;

    protected MenuProductResponse() {
    }

    public MenuProductResponse(long seq, Menu menu, Product product, Quantity quantity) {
        this.seq = seq;
        this.menuId = menu.getId();
        this.productId = product.getId();
        this.quantity = quantity.value();
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(),
                menuProduct.getMenu(),
                menuProduct.getProduct(),
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
