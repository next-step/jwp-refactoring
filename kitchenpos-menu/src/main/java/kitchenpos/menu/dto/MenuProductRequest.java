package kitchenpos.menu.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import kitchenpos.common.domain.Quantity;

public class MenuProductRequest {
    private Long productId;
    private Quantity quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    @JsonGetter("quantity")
    public Long quantity() {
        return quantity.value();
    }
}
