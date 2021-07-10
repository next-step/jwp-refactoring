package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

public class OrderLineItems {
    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validate(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> menuIds() {
        return orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
    }

    public long size() {
        return orderLineItems.size();
    }

    public List<OrderLineItem> toList() {
        return orderLineItems;
    }
}
