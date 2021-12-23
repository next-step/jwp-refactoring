package kitchenpos.event;

import kitchenpos.domain.order.OrderLineItem;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class OrderCreatedEvent extends ApplicationEvent {

    private final List<OrderLineItem> orderLineItems;


    public OrderCreatedEvent(List<OrderLineItem> orderLineItems) {
        super(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
