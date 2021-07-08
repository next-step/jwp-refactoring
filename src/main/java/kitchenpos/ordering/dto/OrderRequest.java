package kitchenpos.ordering.dto;

import kitchenpos.ordering.domain.Ordering;
import kitchenpos.ordering.domain.OrderLineItem;
import kitchenpos.ordering.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private OrderStatus orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems.stream()
                .map(OrderLineItemRequest::from)
                .collect(Collectors.toList());
    }

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems.stream()
                .map(OrderLineItemRequest::from)
                .collect(Collectors.toList());
    }

    public Ordering toEntity() {
        return new Ordering(orderTableId,
                OrderStatus.COOKING,
                LocalDateTime.now(),
                orderLineItems.stream()
                    .map(OrderLineItemRequest::toEntity)
                    .collect(Collectors.toList()));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

}
