package kitchenpos.order.domain;

import kitchenpos.exception.OrderLineItemError;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {

    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        if(CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(OrderLineItemError.CANNOT_EMPTY);
        }
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public void addOrderLineItem(Order order, OrderLineItem orderLineItem) {
        if (!hasOrderLineItem(orderLineItem)) {
            orderLineItems.add(orderLineItem);
            orderLineItem.updateOrder(order);
        }
    }

    private boolean hasOrderLineItem(OrderLineItem orderLineItem) {
        return orderLineItems.contains(orderLineItem);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public void updateOrder(Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.updateOrder(order));
    }
}
