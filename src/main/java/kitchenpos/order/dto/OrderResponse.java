package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final List<OrderLineItemResponse> orderLineItems;
    private final LocalDateTime orderedTime;

    public OrderResponse(Long id, Long orderTableId, String orderStatus, List<OrderLineItemResponse> orderLineItems, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        this.orderedTime = orderedTime;
    }

    public static OrderResponse of(Order order) {
        final List<OrderLineItemResponse> orderLineItems = order.getOrderLineItems().stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(), orderLineItems, order.getOrderedTime());
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderResponse that = (OrderResponse) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (orderTableId != null ? !orderTableId.equals(that.orderTableId) : that.orderTableId != null) return false;
        if (orderStatus != null ? !orderStatus.equals(that.orderStatus) : that.orderStatus != null) return false;
        if (orderLineItems != null ? !orderLineItems.equals(that.orderLineItems) : that.orderLineItems != null)
            return false;
        return orderedTime != null ? orderedTime.equals(that.orderedTime) : that.orderedTime == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (orderTableId != null ? orderTableId.hashCode() : 0);
        result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
        result = 31 * result + (orderLineItems != null ? orderLineItems.hashCode() : 0);
        result = 31 * result + (orderedTime != null ? orderedTime.hashCode() : 0);
        return result;
    }
}
