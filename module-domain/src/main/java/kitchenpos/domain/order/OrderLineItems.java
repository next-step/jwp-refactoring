package kitchenpos.domain.order;

import kitchenpos.common.exception.OrderLineItemNotEmptyException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        update(orderLineItems);
    }

    public void update(List<OrderLineItem> orderLineItems) {
        if (!this.orderLineItems.isEmpty()) {
            throw new OrderLineItemNotEmptyException();
        }

        orderLineItems.forEach(item -> this.orderLineItems.add(item));
    }

    public int size() {
        return orderLineItems.size();
    }

    public List<OrderLineItem> toCollection() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
