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

    protected OrderLineItems() {}

    public List<OrderLineItem> getValue() {
        return orderLineItems;
    }

    public void addAll(Order order, List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItem(order, orderLineItem);
        }
    }

    private void addOrderLineItem(Order order, OrderLineItem orderLineItem) {
        if (!this.orderLineItems.contains(orderLineItem)) {
            this.orderLineItems.add(orderLineItem);
        }
        orderLineItem.addOrder(order);
    }
}
