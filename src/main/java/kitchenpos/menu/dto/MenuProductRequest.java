package kitchenpos.menu.dto;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {

    private long productId;
    private long quantity;

    protected MenuProductRequest() {
    }

    private MenuProductRequest(long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toMenuProduct() {
        return MenuProduct.of(productId, Quantity.of(quantity));
    }
}
