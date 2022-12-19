package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

public class OrderResponse {

    private long id;
    private long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrderResponse() {
    }

    protected OrderResponse(long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTable.getId();
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems()
                .getUnmodifiableOrderLineItems()
                .stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderTable(), order.getOrderStatus(), order.getOrderedTime(),
                orderLineItemResponses);
    }

    public long getId() {
        return id;
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
