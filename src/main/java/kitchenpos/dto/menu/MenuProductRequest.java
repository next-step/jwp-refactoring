package kitchenpos.dto.menu;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(final Long productId, final long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public MenuProduct toMenuProduct(final Product product) {
        return MenuProduct.of(product, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
