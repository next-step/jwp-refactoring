package kitchenpos.order.dto;

import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private long seq;
    private long orderId;
    private long menuId;
    private long quantity;

    protected OrderLineItemResponse() {
    }

    protected OrderLineItemResponse(long seq, Order order, long menuId, Quantity quantity) {
        this.seq = seq;
        this.orderId = order.getId();
        this.menuId = menuId;
        this.quantity = quantity.value();
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
                orderLineItem.getOrder(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity());
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
