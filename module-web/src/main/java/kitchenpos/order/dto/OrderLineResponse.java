package kitchenpos.order.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineResponse {
    private long menuId;
    private long quantity;

    public OrderLineResponse() {
    }

    public OrderLineResponse(long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public static OrderLineResponse of(OrderLineItem lineItems) {
        return new OrderLineResponse(lineItems.getMenuId(), lineItems.getQuantity());
    }
}
