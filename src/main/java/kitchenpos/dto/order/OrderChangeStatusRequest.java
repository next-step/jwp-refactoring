package kitchenpos.dto.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderLineItem.OrderLineItems;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.dto.orderLineItem.OrderLineItemRequest;

import java.util.List;

public class OrderChangeStatusRequest {
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderChangeStatusRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder(OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(null, orderTable, orderStatus, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
