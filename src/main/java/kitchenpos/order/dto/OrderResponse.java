package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.table.dto.OrderTableResponse;

public class OrderResponse {
    private Long id;
    private OrderTableResponse orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrderResponse() {
    }

    public OrderResponse(Order order) {
        this(
            order.getId(),
            OrderTableResponse.from(order.getOrderTable()),
            order.getOrderStatus(),
            order.getOrderedTime(),
            OrderLineItemResponse.toList(order.getOrderLineItems()));
    }

    public OrderResponse(Long id, OrderTableResponse orderTable, String orderStatus,
        LocalDateTime orderedTime,
        List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            OrderTableResponse.from(order.getOrderTable()),
            order.getOrderStatus(),
            order.getOrderedTime(),
            OrderLineItemResponse.toList(order.getOrderLineItems())
        );
    }
}
