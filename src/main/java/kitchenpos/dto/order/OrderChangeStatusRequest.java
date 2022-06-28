package kitchenpos.dto.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderLineItem.OrderLineItems;
import kitchenpos.domain.orderTable.OrderTable;

public class OrderChangeStatusRequest {
    private final OrderStatus orderStatus;

    public OrderChangeStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order toOrder(OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(null, orderTable, orderStatus, orderLineItems);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
