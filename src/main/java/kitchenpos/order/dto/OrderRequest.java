package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderRequest {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(final Long orderTableId, final String orderStatus, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderRequest setId(final Long id) {
        this.id = id;
        return this;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public List<Long> getMenuIds() {
        return this.orderLineItems.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(toList());
    }

    public List<OrderLineItem> toOrderLineItemList() {
        return this.orderLineItems.stream()
            .map(OrderLineItemRequest::toOrderLineItem)
            .collect(toList());
    }
}
