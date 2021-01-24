package kitchenpos.order.util;

import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderRequest;

import java.util.ArrayList;
import java.util.List;

public class OrderRequestBuilder {
    private long orderTableId;
    private final List<OrderLineRequest> orderLineItems = new ArrayList<>();

    public OrderRequestBuilder withOrderTableId(long orderTableId) {
        this.orderTableId = orderTableId;
        return this;
    }

    public OrderRequestBuilder addOrderLineItem(long menuId, int quantity) {
        this.orderLineItems.add(new OrderLineRequest(menuId, quantity));
        return this;
    }

    public OrderRequest build() {
        return new OrderRequest(orderTableId, orderLineItems);
    }
}
