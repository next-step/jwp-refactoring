package kitchenpos.order.domain;

import java.util.*;

import javax.persistence.*;

import kitchenpos.menu.domain.*;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public void add(Order order, Menu menu, int quantity) {
        orderLineItems.add(new OrderLineItem(order, menu, quantity));
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}

