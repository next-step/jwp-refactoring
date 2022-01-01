package kitchenpos.dto.menu;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private long productId;
    private long quantity;

    protected MenuProductResponse() {
    }

    private MenuProductResponse(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getProductId(), menuProduct.getQuantity().toLong());
    }

    public static MenuProductResponse of(final Long productId, final long quantity) {
        return new MenuProductResponse(productId, quantity);
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
