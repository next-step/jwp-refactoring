package kitchenpos.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.response.OrderTableResponse;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
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

    private OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public OrderResponse() {
    }

    public static OrderResponse of(Order order, OrderTable orderTable) {
        List<OrderLineItemResponse> orderLineItems = order.getOrderLineItems().stream()
            .map(OrderLineItemResponse::of)
            .collect(Collectors.toList());

        return new OrderResponse(
            order.getId(),
            OrderTableResponse.of(orderTable),
            order.getOrderStatus(),
            order.getOrderedTime(),
            orderLineItems
        );
    }

    public static OrderResponse toResponseWithoutOrderTable(Order order) {
        List<OrderLineItemResponse> orderLineItems = order.getOrderLineItems().stream()
            .map(OrderLineItemResponse::of)
            .collect(Collectors.toList());

        return new OrderResponse(
            order.getId(),
            order.getOrderTableId(),
            order.getOrderStatus(),
            order.getOrderedTime(),
            orderLineItems
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
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
