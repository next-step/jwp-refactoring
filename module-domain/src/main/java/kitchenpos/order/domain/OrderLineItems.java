package kitchenpos.order.domain;

import java.util.*;

import javax.persistence.*;

import kitchenpos.menu.domain.*;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public void add(Long orderId, Menu menu, int quantity) {
        orderLineItems.add(new OrderLineItem(orderId, menu, quantity));
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}

