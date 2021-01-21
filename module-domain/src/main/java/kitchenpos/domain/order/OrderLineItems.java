package kitchenpos.domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    protected OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems createInstance() {
        return new OrderLineItems();
    }

    public static OrderLineItems of(final List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public void add(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public void linkOrderId(final Long orderId) {
        this.orderLineItems.forEach(orderLineItem -> orderLineItem.linkOrderId(orderId));
    }

    public List<OrderLineItem> orderLineItems() {
        return Collections.unmodifiableList(this.orderLineItems);
    }
}
