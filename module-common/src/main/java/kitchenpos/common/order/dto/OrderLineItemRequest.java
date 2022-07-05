package kitchenpos.common.order.dto;

import kitchenpos.common.common.domain.Quantity;

public class OrderLineItemRequest {
    private Long menuId;
    private Quantity quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId,
                                final Quantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
