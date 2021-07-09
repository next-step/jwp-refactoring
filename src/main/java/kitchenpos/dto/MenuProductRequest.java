package kitchenpos.dto;

import kitchenpos.domain.Quantity;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Quantity quantity() {
        return Quantity.valueOf(quantity);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
