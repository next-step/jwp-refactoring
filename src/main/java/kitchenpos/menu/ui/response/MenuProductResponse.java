package kitchenpos.menu.ui.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;

public final class MenuProductResponse {

    private long seq;
    private long menuId;
    private long productId;
    private long quantity;

    private MenuProductResponse() {
    }

    private MenuProductResponse(long seq, long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    static List<MenuProductResponse> listFrom(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toList());
    }

    public long getSeq() {
        return seq;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    private static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(
            menuProduct.getSeq(),
            menuProduct.getProductId(),
            menuProduct.getQuantity()
        );
    }
}
