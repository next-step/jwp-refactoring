package kitchenpos.domain.order;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderLineItem> orderLineItems = new HashSet<>();

    protected OrderLineItems() {}

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new HashSet<>(orderLineItems);
    }

    public static OrderLineItems createOrderLineItems(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public Set<OrderLineItem> getValue() {
        return orderLineItems;
    }

    public void addAll(Order order, OrderLineItems orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems.getValue()) {
            addOrderLineItem(order, orderLineItem);
        }
    }

    private void addOrderLineItem(Order order, OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
        orderLineItem.addOrder(order);
    }
}
