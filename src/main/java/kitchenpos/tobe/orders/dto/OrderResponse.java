package kitchenpos.tobe.orders.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tobe.orders.domain.order.Order;

public class OrderResponse {

    private Long id;

    private Long orderTableId;

    private String status;

    private LocalDateTime orderedDateTime;

    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(
        final Long id,
        final Long orderTableId,
        final String status,
        final LocalDateTime orderedDateTime,
        final List<OrderLineItemResponse> orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.status = status;
        this.orderedDateTime = orderedDateTime;
        this.orderLineItems = orderLineItems;
    }

    public static List<OrderResponse> ofList(final List<Order> orders) {
        return orders.stream()
            .map(OrderResponse::of)
            .collect(Collectors.toList());
    }

    public static OrderResponse of(final Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOrderTableId(),
            order.getStatus().name(),
            order.getOrderedDateTime(),
            OrderLineItemResponse.ofList(order.getOrderLineItems())
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getOrderedDateTime() {
        return orderedDateTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
