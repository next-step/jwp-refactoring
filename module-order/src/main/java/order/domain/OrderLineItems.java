package order.domain;

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

    public void addAll(Order order, List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        for (OrderLineItem orderLineItem : orderLineItems) {
            add(order, orderLineItem);
        }
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문내역 목록이 비어있습니다.");
        }
    }

    private void add(Order order, OrderLineItem orderLineItem) {
        if (!this.orderLineItems.contains(orderLineItem)) {
            this.orderLineItems.add(orderLineItem);
        }
        orderLineItem.bindTo(order);
    }

    public List<OrderLineItem> get() {
        return orderLineItems;
    }
}
