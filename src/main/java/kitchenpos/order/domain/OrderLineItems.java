package kitchenpos.order.domain;

import kitchenpos.ExceptionMessage;

import java.util.List;

public class OrderLineItems {

    private List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_LINE_ITEMS_EMPTY.getMessage());
        }
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItem> getValue() {
        return orderLineItems;
    }

}
