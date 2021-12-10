package kitchenpos.menu.ui.response;

import java.util.List;
import kitchenpos.domain.MenuProduct;

public final class MenuProductResponse {

    private long seq;
    private long menuId;
    private long productId;
    private long quantity;

    private MenuProductResponse() {
    }

    private MenuProductResponse(long seq, long menuId, long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> listFrom(List<MenuProduct> menuProducts) {
        return null;
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
