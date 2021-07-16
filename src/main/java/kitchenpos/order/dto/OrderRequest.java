package kitchenpos.order.dto;

import kitchenpos.order.enums.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemRequests = orderLineItemRequests;
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

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public List<Long> toMenuIds() {
        return orderLineItemRequests.stream().map(OrderLineItemRequest::getMenuId).collect(Collectors.toList());
    }

    public Orders createNewOrder() {
        return new Orders(orderTableId, OrderStatus.COOKING, LocalDateTime.now());
    }

    public List<OrderLineItem> toOrderLineItems() {
        return orderLineItemRequests.stream().map(item -> item.toOrderLineItem()).collect(Collectors.toList());
    }
}
