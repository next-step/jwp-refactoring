package kitchenpos.order.dto;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private long seq;
    private long orderId;
    private long menuId;
    private long quantity;

    protected OrderLineItemResponse() {
    }

    protected OrderLineItemResponse(long seq, Order order, Menu menu, Quantity quantity) {
        this.seq = seq;
        this.orderId = order.getId();
        this.menuId = menu.getId();
        this.quantity = quantity.value();
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
                orderLineItem.getOrder(),
                orderLineItem.getMenu(),
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
