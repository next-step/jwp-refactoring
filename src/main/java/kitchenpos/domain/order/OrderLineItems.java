package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    public List<OrderLineItem> orderLineItems() {
        return orderLineItems;
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        if (!orderLineItems.contains(orderLineItem)) {
            orderLineItems.add(orderLineItem);
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void mapOrder(Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.orderedBy(order));
    }
}
