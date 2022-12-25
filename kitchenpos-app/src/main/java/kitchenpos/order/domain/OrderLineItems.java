package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {

    }

    public void addOrderLineItem(Order order, OrderLineItem orderLineItem) {
        if (!hasOrderLineItem(orderLineItem)) {
            orderLineItems.add(orderLineItem);
            orderLineItem.updateOrder(order);
        }
    }

    private boolean hasOrderLineItem(OrderLineItem orderLineItem) {
        return orderLineItems.contains(orderLineItem);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
