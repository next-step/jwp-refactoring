package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.model.Order;
import kitchenpos.domain.model.OrderLineItem;
import kitchenpos.domain.model.OrderStatus;

public class OrderDto {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems;

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

    public OrderDto() {
    }

    public OrderDto(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
            List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderDto of(Order order) {
        List<OrderLineItemDto> collect = order.getOrderListItems().stream()
                .map(it -> OrderLineItemDto.of(it, order.getId()))
                .collect(Collectors.toList());
        return new OrderDto(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(), order.getOrderedTime(), collect);
    }

    public Order toEntity() {
        List<OrderLineItem> collect = orderLineItems.stream()
                .map(OrderLineItemDto::toEntity)
                .collect(Collectors.toList());
        return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), collect);
    }
}
