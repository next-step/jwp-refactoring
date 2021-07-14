package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private final List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
        orderLineItems = new ArrayList<>();
    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public List<OrderLineItem> getUnmodifiableList() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public void registerAll(Long orderId) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.registerOrder(orderId));
    }
}
