package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public static OrderResponse of(Order order) {
        OrderResponse orderResponse = new OrderResponse(order.getId(), order.getOrderStatus(), order.getOrderedTime());

        OrderTable orderTable = order.getOrderTable();
        if (orderTable != null) {
            orderResponse.setOrderTableId(orderTable.getId());
        }
        orderResponse.setOrderLineItems(order.getOrderLineItems());
        return orderResponse;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public OrderResponse() {
    }

    public OrderResponse(Long id, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }
}
