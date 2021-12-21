package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public final class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    protected void add(OrderLineItem orderLineItem) {
        if (!contains(orderLineItem)) {
            orderLineItems.add(orderLineItem);
        }
    }

    private boolean contains(OrderLineItem orderLineItem) {
        return orderLineItems.stream()
            .anyMatch(it -> it.equalsOrderLineItem(orderLineItem));
    }

    public void remove(OrderLineItem orderLineItem) {
        orderLineItem.removeOrder();
        this.orderLineItems.remove(orderLineItem);
    }

    public List<OrderLineItem> get() {
        return orderLineItems;
    }
}
