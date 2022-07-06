package kitchenpos.menu.dto;

import static kitchenpos.common.message.ValidationMessage.POSITIVE;

import javax.validation.constraints.Positive;

public class MenuProductRequest {
    @Positive(message = POSITIVE)
    private Long productId;

    @Positive(message = POSITIVE)
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
