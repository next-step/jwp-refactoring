package kitchenpos.order.domain;

import org.springframework.context.ApplicationEvent;

public class OrderCreatedEvent extends ApplicationEvent {


    private final Long orderTableId;

    public OrderCreatedEvent(Long orderTableId) {
        super(orderTableId);
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
