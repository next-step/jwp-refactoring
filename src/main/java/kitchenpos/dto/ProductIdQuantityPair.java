package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import kitchenpos.domain.Quantity;

public class ProductIdQuantityPair {
    private Long productId;
    private Quantity quantity;

    public ProductIdQuantityPair() {
    }

    public ProductIdQuantityPair(Long productId, Quantity quantity) {
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
