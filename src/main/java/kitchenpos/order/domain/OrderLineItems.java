package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
