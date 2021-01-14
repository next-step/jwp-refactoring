package kitchenpos.order.domain;

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

    public OrderLineItems(final List<OrderLineItem> orderLineItems, final Order order) {
        this.orderLineItems.addAll(orderLineItems);
        assign(order);
    }

    private void assign(final Order order) {
        orderLineItems.forEach(menuProduct -> menuProduct.assign(order));
    }

    public int size() {
        return orderLineItems.size();
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
