package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();


    protected OrderLineItems() {

    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void toOrder(final Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.toOrder(order));
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
