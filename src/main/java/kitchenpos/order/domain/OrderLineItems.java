package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void add(Order order, Menu menu, long quantity) {
        orderLineItems.add(new OrderLineItem(order, menu, quantity));
    }

    public List<OrderLineItem> values() {
        return orderLineItems;
    }
}
