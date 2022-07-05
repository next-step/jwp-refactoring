package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dto.OrderTableResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(Long id, OrderTableResponse orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public OrderResponse() {
    }

    public static OrderResponse of(Order order) {
        List<OrderLineItemResponse> orderLineItems = order.getOrderLineItems().stream()
            .map(OrderLineItemResponse::toResponseWithoutOrder)
            .collect(Collectors.toList());

        return new OrderResponse(
            order.getId(),
            OrderTableResponse.of(order.getOrderTable()),
            order.getOrderStatus(),
            order.getOrderedTime(),
            orderLineItems
        );
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTable() {
        return orderTable;
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
