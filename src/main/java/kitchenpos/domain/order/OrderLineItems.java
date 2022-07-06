package kitchenpos.domain.order;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {

    }

    private OrderLineItems(Order order, List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        setOrder(order);
    }

    public static OrderLineItems of(Order order, List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(order, orderLineItems);
    }

    public void setOrder(Order order) {
        orderLineItems.forEach(it -> it.setOrder(order));
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
