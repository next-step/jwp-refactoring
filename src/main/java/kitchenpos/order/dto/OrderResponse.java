package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(),
                order.getOrderTableId(),
                order.getOrderStatusName(),
                order.getOrderedTime(),
                toOrderLineItemResponses(order.getOrderLineItems().values()));
    }

    public static List<OrderLineItemResponse> toOrderLineItemResponses(List<OrderLineItem> orderLineItems) {
        return orderLineItems
                .stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOrderTableId(), that.getOrderTableId()) && Objects.equals(getOrderStatus(), that.getOrderStatus()) && Objects.equals(getOrderedTime(), that.getOrderedTime()) && Objects.equals(getOrderLineItems(), that.getOrderLineItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderTableId(), getOrderStatus(), getOrderedTime(), getOrderLineItems());
    }
}
