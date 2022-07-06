package kitchenpos.order.dto;

import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems = new ArrayList<>();

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long orderTableId,
                         OrderStatus orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(),
                                    order.getOrderTableId(),
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

    public Long getOrderTableId() {
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
