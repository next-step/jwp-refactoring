package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderLineItems)) {
            return false;
        }
        OrderLineItems orderLineItems = (OrderLineItems) o;
        return Objects.equals(this.orderLineItems, orderLineItems.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderLineItems);
    }
}
