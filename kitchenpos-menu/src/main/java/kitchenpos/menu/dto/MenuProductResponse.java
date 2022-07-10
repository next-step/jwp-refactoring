package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;

public class MenuProductResponse {
    private Long productId;
    private long quantity;

    private MenuProductResponse() {
    }

    public MenuProductResponse(MenuProduct menuProduct) {
        this(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public MenuProductResponse(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
