package kitchenpos.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.utils.StreamUtils;

public class OrdersResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrdersResponse() {}

    private OrdersResponse(Long id,
                           Long orderTableId,
                           OrderStatus orderStatus,
                           LocalDateTime orderedTime,
                           List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrdersResponse from(Orders orders) {
        return new OrdersResponse(orders.getId(),
                                  orders.getOrderTable().getId(),
                                  orders.getOrderStatus(),
                                  orders.getOrderedTime(),
                                  StreamUtils.mapToList(orders.getOrderLineItems(), OrderLineItemResponse::from));
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrdersResponse that = (OrdersResponse)o;
        return Objects.equals(id, that.id) && Objects.equals(orderTableId, that.orderTableId)
            && orderStatus == that.orderStatus && Objects.equals(orderedTime, that.orderedTime)
            && Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
