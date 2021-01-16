package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems, Order order) {
        this.orderLineItems.addAll(orderLineItems);
        updateOrder(order);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public boolean isRegisteredMenuNotMore(int minCount) {
        long count = orderLineItems.stream()
                .map(OrderLineItem::getMenu)
                .count();
        return count < minCount;
    }

    private void updateOrder(Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.updateOrder(order));
    }
}
