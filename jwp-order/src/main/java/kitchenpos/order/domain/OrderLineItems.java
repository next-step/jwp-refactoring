package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderLineItems {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    private OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public static OrderLineItems from(final List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public static OrderLineItems empty() {
        return from(new ArrayList<>());
    }

    public void add(final OrderLineItem item) {
        orderLineItems.add(item);
    }

    public List<OrderLineItem> toList() {
        return Collections.unmodifiableList(orderLineItems);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItems that = (OrderLineItems) o;
        return Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderLineItems);
    }
}
