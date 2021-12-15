package kitchenpos.dto.order;

import java.util.ArrayList;
import java.util.List;

import kitchenpos.domain.order.OrderStatus;

public class OrdersRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

    protected OrdersRequest() {}

    public OrdersRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static OrdersRequest of(OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        return new OrdersRequest(null, orderStatus, orderLineItems);
    }

    public static OrdersRequest of(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        return new OrdersRequest(orderTableId, orderStatus, orderLineItems);
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
