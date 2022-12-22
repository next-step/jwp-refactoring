package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
        orderLineItems = new ArrayList<>();
    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = Collections.unmodifiableList(orderLineItems);
    }

    public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void updateOrder(Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.updateOrder(order));
    }

    public int getSize() {
        return orderLineItems.size();
    }
}
