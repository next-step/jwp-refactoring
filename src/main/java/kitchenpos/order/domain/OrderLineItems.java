package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderLineItems {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private final List<OrderLineItem> collections;

    protected OrderLineItems() {
        collections = new ArrayList<>();
    }

    public void add(final OrderLineItem orderLineItem) {
        collections.add(orderLineItem);
    }

    public List<OrderLineItem> get() {
        return Collections.unmodifiableList(collections);
    }

    @Override
    public String toString() {
        return "OrderLineItems{" +
                "collections=" + collections +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderLineItems that = (OrderLineItems) o;
        return Objects.equals(collections, that.collections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collections);
    }
}
