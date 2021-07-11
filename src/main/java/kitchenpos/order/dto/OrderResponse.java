package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems;

    public OrderResponse() {}

    public OrderResponse(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                         final List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order) {
        List<OrderLineItemDto> orderLineItems = order.getOrderLineItems()
                .stream()
                .map(OrderLineItemDto::from)
                .collect(toList());

        return new OrderResponse(order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                orderLineItems);
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

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
