package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class OrderLineItems {
    private final List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
        orderLineItems = new ArrayList<>();
    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }
    
    public static OrderLineItems of(OrderLineItem ... orderLineItems) {
        return new OrderLineItems(Arrays.asList(orderLineItems));
    }

    public int size() {
        return this.orderLineItems.size();
    }

    public boolean isEmpty() {
        return this.orderLineItems.isEmpty();
    }

    public void acceptOrder(Orders order) {
        this.orderLineItems.stream().forEach(
            orderLineItem -> orderLineItem.acceptOrder(order)
        );
    }

}
