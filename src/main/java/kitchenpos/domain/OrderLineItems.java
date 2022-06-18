package kitchenpos.domain;

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
    private List<OrderLineItemEntity> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public void addAll(Order order, List<OrderLineItemEntity> orderLineItems) {
        requireNonNull(order, "order");
        requireNonNull(orderLineItems, "orderLineItems");
        for (OrderLineItemEntity orderLineItem : orderLineItems) {
            add(order, orderLineItem);
        }
    }

    public void add(Order order, OrderLineItemEntity orderLineItem) {
        if (!this.orderLineItems.contains(orderLineItem)) {
            this.orderLineItems.add(orderLineItem);
        }
        orderLineItem.bindTo(order);
    }

    public List<OrderLineItemEntity> get() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
