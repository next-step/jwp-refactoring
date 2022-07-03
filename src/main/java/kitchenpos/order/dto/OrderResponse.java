package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dto.OrderTableResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private OrderTableResponse orderTableResponse;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLines = new ArrayList<>();

    protected OrderResponse() {}

    public OrderResponse(
            Long id,
            OrderTableResponse orderTableResponse,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItemResponse> orderLines
    ) {
        this.id = id;
        this.orderTableResponse = orderTableResponse;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLines.addAll(orderLines);
    }

    public static OrderResponse from(Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems()
                .getValue()
                .stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                OrderTableResponse.from(order.getOrderTable()),
                order.getOrderStatus(),
                order.getOrderedTime(),
                orderLineItemResponses
        );
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTableResponse() {
        return orderTableResponse;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLines() {
        return orderLines;
    }
}
