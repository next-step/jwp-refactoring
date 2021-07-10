package kitchenpos.ordering.dto;

import kitchenpos.ordering.domain.OrderStatus;
import kitchenpos.ordering.domain.Ordering;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItemResponses;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItemResponses, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus.name();
        this.orderedTime = orderedTime;
        this.orderLineItemResponses = orderLineItemResponses;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static OrderResponse of(Ordering order) {
        return new OrderResponse(order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                order.getOrderLineItems().stream()
        .map(OrderLineItemResponse::of)
        .collect(Collectors.toList()),
                order.getCreatedDate(),
                order.getModifiedDate());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(orderStatus);
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItemResponses;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(orderTableId, that.orderTableId) && Objects.equals(orderStatus, that.orderStatus) && Objects.equals(orderedTime, that.orderedTime) && Objects.equals(orderLineItemResponses, that.orderLineItemResponses) && Objects.equals(createdDate, that.createdDate) && Objects.equals(modifiedDate, that.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItemResponses, createdDate, modifiedDate);
    }
}
