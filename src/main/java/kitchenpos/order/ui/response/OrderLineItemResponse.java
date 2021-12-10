package kitchenpos.order.ui.response;

import kitchenpos.domain.OrderLineItem;

public final class OrderLineItemResponse {

    private long seq;
    private long orderId;
    private long menuId;
    private long quantity;

    private OrderLineItemResponse() {
    }

    private OrderLineItemResponse(long seq, long orderId, long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return null;
    }

    public long getSeq() {
        return seq;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
