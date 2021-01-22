package kitchenpos.menu.dto;

import kitchenpos.domain.Quantity;

public class MenuProductRequest {
    private final Long productId;
    private final long quantity;

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

    public Quantity ofQuantity() {
        return Quantity.of(quantity);
    }
}
