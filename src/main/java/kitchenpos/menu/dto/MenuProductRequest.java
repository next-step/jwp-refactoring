package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    private MenuProductRequest() {
    }

    public MenuProductRequest(MenuProduct menuProduct) {
        this(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public MenuProductRequest(Long productId, long quantity) {
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
