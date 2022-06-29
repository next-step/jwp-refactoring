package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.domain.OrderTable;

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
