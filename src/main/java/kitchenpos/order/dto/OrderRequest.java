package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.stream()
                .map(orderLineItemRequest -> new OrderLineItem(orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity(), orderLineItemRequest.getOrderLineItemMenuName(),
                        orderLineItemRequest.getOrderLineItemMenuPrice()))
                .collect(Collectors.toList());
    }

    public void setOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
