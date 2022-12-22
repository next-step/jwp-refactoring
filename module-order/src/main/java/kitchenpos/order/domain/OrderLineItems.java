package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public void addOrderLineItem(Order order, OrderLineItem orderLineItem) {
        if (!orderLineItems.contains(orderLineItem)) {
            this.orderLineItems.add(orderLineItem);
            orderLineItem.updateOrder(order);
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
