package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineResponse {
    private final long menuId;
    private final long quantity;

    public OrderLineResponse(long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineResponse of(OrderLineItem lineItems) {
        return new OrderLineResponse(lineItems.getMenuId(), lineItems.getQuantity());
    }
}
