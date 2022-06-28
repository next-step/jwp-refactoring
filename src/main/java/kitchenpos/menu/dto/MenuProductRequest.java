package kitchenpos.menu.dto;

import javax.validation.constraints.Positive;

public class MenuProductRequest {
    private Long productId;

    @Positive
    private long quantity;

    public MenuProductRequest() {
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
