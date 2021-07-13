package kitchenpos.order.domain;

import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ReadOnlyProperty
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

    public void registerAll(Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.registerOrder(order));
    }
}
