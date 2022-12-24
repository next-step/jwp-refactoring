package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.order.exceptions.OrderErrorCode.ORDER_LINE_ITEM_REQUEST;


@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ORDER_LINE_ITEM_REQUEST.getMessage());
        }

        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrder(Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(order));
    }
}
