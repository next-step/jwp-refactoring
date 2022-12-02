package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.ArrayList;
import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItem> orderLineItems;

    public Order toOrder() {
        return new Order(this.orderTableId, orderLineItems);
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public List<OrderLineItem> toOrderLineItems() {
        return new ArrayList<>();
    }
}
