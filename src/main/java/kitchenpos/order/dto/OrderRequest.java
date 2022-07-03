package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems.stream().map(x -> new OrderLineItemRequest(x.getMenuId(), x.getQuantity()))
                .collect(Collectors.toList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::toEntity).collect(Collectors.toList());
    }
}
