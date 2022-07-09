package kitchenpos.order.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(final Long orderTableId,
                        final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    protected OrderRequest() {
    }

    public Order toOrder() {
        final List<OrderLineItem> orderLineItems = this.orderLineItems
                .stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());
        return new Order(orderTableId, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
