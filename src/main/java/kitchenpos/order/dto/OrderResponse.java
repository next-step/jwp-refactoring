package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dto.OrderTableResponse;

public class OrderResponse {
    private Long id;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private OrderTableResponse orderTableResponse;
    private List<OrderLineItemResponse> orderLineItemResponses = new ArrayList<>();

    protected OrderResponse() {
    }

    private OrderResponse(Long id, OrderStatus orderStatus, LocalDateTime orderedTime, OrderTableResponse orderTableResponse, List<OrderLineItemResponse> orderLineItemResponses) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTableResponse = orderTableResponse;
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public static OrderResponse of(Order order) {
        List<OrderLineItemResponse> collect = order.getOrderLineItems()
                .stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderStatusEnum(), order.getOrderedTime(), OrderTableResponse.of(order.getOrderTable()), collect);
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderTableResponse getOrderTableResponse() {
        return orderTableResponse;
    }

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItemResponses;
    }
}
