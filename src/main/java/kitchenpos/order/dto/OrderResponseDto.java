package kitchenpos.order.dto;

import kitchenpos.common.domain.OrderStatus;
import kitchenpos.order.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponseDto {
    private Long id;
    private OrderStatus orderStatus;
    private Long orderTableId;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponseDto> orderLineItems;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus();
        this.orderTableId = order.getOrderTableId();
        this.orderedTime = order.getOrderedTime();
        this.orderLineItems = order.getOrderLineItems().stream()
                .map(OrderLineItemResponseDto::new)
                .collect(Collectors.toList());
    }

    public OrderResponseDto(Long id, OrderStatus orderStatus, Long orderTableId, LocalDateTime orderedTime, List<OrderLineItemResponseDto> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderTableId = orderTableId;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponseDto> getOrderLineItems() {
        return orderLineItems;
    }
}
