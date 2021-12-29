package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.OrderLineItem;

public class OrderRequest {
    private Long orderTableId;
    private String orderStatus;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(Long orderTableId, String orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<OrderLineItem> getOrderLineItemsEntity() {
        return orderLineItems.stream()
            .map(orderLineItemRequest ->
                new OrderLineItem(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
            .collect(Collectors.toList());
    }
}
