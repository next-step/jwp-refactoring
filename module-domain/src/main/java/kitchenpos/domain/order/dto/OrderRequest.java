package kitchenpos.domain.order.dto;

import kitchenpos.domain.order.OrderLineItem;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderRequest {
    private static final String ERR_TEXT_INVALID_ORDER = "유효하지 않은 주문 정보입니다.";

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
        if (orderLineItems == null || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ERR_TEXT_INVALID_ORDER);
        }

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
