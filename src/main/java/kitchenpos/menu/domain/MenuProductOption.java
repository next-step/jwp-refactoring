package kitchenpos.menu.domain;

import kitchenpos.generic.quantity.domain.Quantity;

public class MenuProductOption {
    private Long productId;
    private Quantity quantity;

    public MenuProductOption(Long productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
