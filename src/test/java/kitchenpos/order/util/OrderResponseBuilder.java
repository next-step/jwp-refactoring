package kitchenpos.order.util;

import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderLineResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

import java.util.ArrayList;
import java.util.List;

public class OrderResponseBuilder {
    private long orderId;
    private String orderStatus;
    private String orderedTime;
    private final List<OrderLineResponse> orderLineItems = new ArrayList<>();

    public OrderResponseBuilder withOrderId(long orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderResponseBuilder withOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderResponseBuilder withOrderedTime(String orderedTime) {
        this.orderedTime = orderedTime;
        return this;
    }

    public OrderResponseBuilder addOrderLineItem(long menuId, int quantity) {
        this.orderLineItems.add(new OrderLineResponse(menuId, quantity));
        return this;
    }

    public OrderResponse build() {
        return new OrderResponse(orderId, orderStatus, orderedTime, orderLineItems);
    }
}
