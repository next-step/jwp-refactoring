package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

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

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toEntity() {
        return Order.createOrder(orderTableId, mapToOrderLineItems());
    }

    private OrderLineItems mapToOrderLineItems() {
        List<OrderLineItem> mapToOrderLineItems = orderLineItems.stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());
        return OrderLineItems.createOrderLineItems(mapToOrderLineItems);
    }
}
