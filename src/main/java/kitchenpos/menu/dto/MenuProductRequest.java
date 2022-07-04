package kitchenpos.menu.dto;

import kitchenpos.common.domain.Quantity;

public class MenuProductRequest {
    private Long productId;

    private Quantity quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(final Long productId,
                              final Quantity quantity) {
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
