package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public void addAll(Order order, List<OrderLineItem> orderLineItems) {
        requireNonNull(order, "order");
        requireNonNull(orderLineItems, "orderLineItems");
        for (OrderLineItem orderLineItem : orderLineItems) {
            add(order, orderLineItem);
        }
    }

    private void add(Order order, OrderLineItem orderLineItem) {
        if (!this.orderLineItems.contains(orderLineItem)) {
            this.orderLineItems.add(orderLineItem);
        }
        orderLineItem.bindTo(order);
    }

    public List<OrderLineItem> get() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
