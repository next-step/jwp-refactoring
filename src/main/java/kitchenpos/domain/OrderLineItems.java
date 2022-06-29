package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems from(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public void connectToOrder(Order order) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.connectTo(order);
        }
    }

    public List<OrderLineItem> getValues() {
        return Collections.unmodifiableList(orderLineItems);
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목 목록이 있어야 합니다.");
        }
    }
}
