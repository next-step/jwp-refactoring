package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }


    public void addOrderLineItems(OrderLineItems orderLineItems, Order order) {
        for (OrderLineItem orderLineItem : orderLineItems.orderLineItems) {
            orderLineItem.setOrder(order);
            this.orderLineItems.add(orderLineItem);
        }

    }

    public void addOrderLineItems(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    public boolean isEmpty() {
        return orderLineItems.isEmpty();
    }

    public int size() {
        return orderLineItems.size();
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
