package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderExceptionConstants;
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

    protected OrderLineItems() {}

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }
    public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    private void validate(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(OrderExceptionConstants.ORDER_LINE_ITEMS_CANNOT_BE_EMPTY.getErrorMessage());
        }
    }

    public void addOrderLineItem(Order order, OrderLineItem orderLineItem) {
        if (!hasOrderLineItem(orderLineItem)) {
            this.orderLineItems.add(orderLineItem);
            orderLineItem.updateOrder(order);
        }
    }

    private boolean hasOrderLineItem(OrderLineItem orderLineItem) {
        return this.orderLineItems.contains(orderLineItem);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
