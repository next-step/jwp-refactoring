package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrderStatus;

public class OrderDto {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems;

    protected OrderDto() {
    }

    private OrderDto(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderDto of(Long orderTableId, List<OrderLineItemDto> orderLineItems) {
        return new OrderDto(null, orderTableId, "", null, orderLineItems);
    }

    public static OrderDto of(Orders order) {
        List<OrderLineItemDto> tempOrderLineItems = order.getOrderLineItems().stream()
                                                            .map(OrderLineItemDto::of)
                                                            .collect(Collectors.toList());

        if (order.getOrderStatus() == null) {
            return new OrderDto(order.getId(), order.getOrderTable().getId(), "", order.getOrderedTime(), tempOrderLineItems);
        }

        return new OrderDto(order.getId(), order.getOrderTable().getId(), order.getOrderStatus().name(), order.getOrderedTime(), tempOrderLineItems);
    }

    public Long getId() {
        return this.id;
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return this.orderLineItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus.name();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderDto)) {
            return false;
        }
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(id, orderDto.id) && Objects.equals(orderTableId, orderDto.orderTableId) && Objects.equals(orderStatus, orderDto.orderStatus) && Objects.equals(orderedTime, orderDto.orderedTime) && Objects.equals(orderLineItems, orderDto.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
