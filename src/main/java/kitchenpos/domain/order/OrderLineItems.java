package kitchenpos.domain.order;

import kitchenpos.exception.OrderLineItemNotEmptyException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        update(orderLineItems);
    }

    public int size() {
        return orderLineItems.size();
    }

    public List<OrderLineItem> toCollection() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public void update(List<OrderLineItem> orderLineItems) {
        if (this.orderLineItems.size() > 0) {
            throw new OrderLineItemNotEmptyException();
        }

        orderLineItems.forEach(item -> this.orderLineItems.add(item));
    }
}
