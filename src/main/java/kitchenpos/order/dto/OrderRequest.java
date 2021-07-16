package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

public class OrderRequest {
    private Long id;
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public void setId(final Long id) {
        this.id = id;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public void setOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        OrderLineItem[] orderLineItemArray = orderLineItems.stream()
                .map(x -> x.toEntity())
                .toArray(OrderLineItem[]::new);

        return new Order(id, orderTableId, orderLineItemArray);
    }
}
