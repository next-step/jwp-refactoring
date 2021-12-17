package kitchenpos.order.domain.orderLineItem;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.order.domain.order.Order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public void addOrderLineItem(Order order, Menu menu, int quantity) {
        orderLineItems.add(new OrderLineItem(order, menu, quantity));
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
