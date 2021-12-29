package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineResponse> orderLineItems;

    public static OrderResponse of(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOrderTableId(),
            order.getOrderStatus().name(),
            order.getOrderedTime(),
            OrderLineResponse.ofList(order.getOrderLineItems()));
    }

    public static List<OrderResponse> ofList(List<Order> orders) {
        return orders.stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    private OrderResponse(Long id, Long orderTableId, String orderStatus,
        LocalDateTime orderedTime,
        List<OrderLineResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
