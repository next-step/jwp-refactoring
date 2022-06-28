package kitchenpos.order.dto;

import javax.validation.constraints.Positive;

public class OrderLineItemRequest {
    private Long menuId;

    @Positive
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
