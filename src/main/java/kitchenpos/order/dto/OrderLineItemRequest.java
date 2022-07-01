package kitchenpos.order.dto;

import static kitchenpos.common.ValidationMessage.POSITIVE;

import javax.validation.constraints.Positive;

public class OrderLineItemRequest {
    @Positive(message = POSITIVE)
    private Long menuId;

    @Positive(message = POSITIVE)
    private Long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
