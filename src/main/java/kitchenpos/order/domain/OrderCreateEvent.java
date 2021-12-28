package kitchenpos.order.domain;

import org.springframework.context.ApplicationEvent;

public class OrderCreateEvent extends ApplicationEvent {

    private Long orderTableId;

    public OrderCreateEvent(Object source, Long orderTableId) {
        super(source);
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
