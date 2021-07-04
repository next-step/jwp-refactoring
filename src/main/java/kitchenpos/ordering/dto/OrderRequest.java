package kitchenpos.ordering.dto;

import kitchenpos.ordering.domain.Ordering;
import kitchenpos.ordering.domain.OrderLineItem;
import kitchenpos.ordering.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems.stream()
                .map(orderLineItem -> OrderLineItemRequest.from(orderLineItem))
                .collect(Collectors.toList());
    }

    public OrderRequest(Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems.stream()
                .map(orderLineItem -> OrderLineItemRequest.from(orderLineItem))
                .collect(Collectors.toList());
    }

    public Ordering toEntity() {
        return new Ordering(orderTableId,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderLineItems.stream()
                    .map(orderLineItemRequest -> orderLineItemRequest.toEntity())
                    .collect(Collectors.toList()));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
