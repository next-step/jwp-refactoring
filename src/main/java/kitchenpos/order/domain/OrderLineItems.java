package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    public OrderLineItems() {
        this.orderLineItems = new ArrayList<>();
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }


    public void registerOrder(Order order) {
        orderLineItems.forEach(
                orderLineItem -> orderLineItem.registerOrder(order)
        );
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
