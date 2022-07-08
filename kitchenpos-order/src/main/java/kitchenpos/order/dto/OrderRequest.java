package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public Order toOrder() {
        List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());
        return new Order(orderTableId, orderStatus, orderLineItems);
    }
}
