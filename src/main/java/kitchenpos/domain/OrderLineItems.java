package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }


    public OrderLineItems(Order order, List<OrderLineItem> orderLineItems) {
        this(orderLineItems);
        orderLineItems.forEach(item -> item.changeOrder(order));
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public int size() {
        return orderLineItems.size();
    }

    public List<OrderLineItem> toCollection() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
