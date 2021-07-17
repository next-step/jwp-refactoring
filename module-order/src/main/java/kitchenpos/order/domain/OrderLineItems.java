package kitchenpos.order.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems, Order order) {
        this.orderLineItems = orderLineItems;
        for (OrderLineItem orderLineItem : this.orderLineItems) {
            orderLineItem.toOrder(order);
        }

    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
