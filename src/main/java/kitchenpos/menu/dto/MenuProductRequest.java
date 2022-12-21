package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductQuantity;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest(MenuProduct menuProduct) {
        this(menuProduct.getProductId(), menuProduct.getQuantityValue());
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(productId, new MenuProductQuantity(quantity));
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
