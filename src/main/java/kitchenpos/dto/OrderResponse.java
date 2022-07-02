package kitchenpos.dto;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private OrderTableResponse orderTableResponse;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems = new ArrayList<>();

    public OrderResponse() {
    }

    public OrderResponse(Long id, OrderTableResponse orderTableResponse,
                         OrderStatus orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableResponse = orderTableResponse;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(),
                                    OrderTableResponse.from(order.getOrderTable()),
                                    order.getOrderStatus(),
                                    order.getOrderedTime(),
                                    OrderLineItemResponse.ofResponses(order.getOrderLineItems())
                                    );
    }

    public static List<OrderResponse> ofResponses(List<Order> orders) {
        return orders.stream()
                    .map(OrderResponse::from)
                    .collect(Collectors.toList());
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
