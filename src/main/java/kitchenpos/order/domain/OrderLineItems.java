package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItems {
    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public List<Long> toMenuIds() {
        return orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
    }
}
