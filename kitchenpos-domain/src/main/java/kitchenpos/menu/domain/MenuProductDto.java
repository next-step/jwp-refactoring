package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;

public class MenuProductDto {

    private Long productId;
    private Quantity quantity;

    public MenuProductDto(Long productId, Quantity quantity) {
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
