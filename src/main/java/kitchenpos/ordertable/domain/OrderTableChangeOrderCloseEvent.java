package kitchenpos.ordertable.domain;

import org.springframework.context.ApplicationEvent;

public class OrderTableChangeOrderCloseEvent extends ApplicationEvent {

    private Long orderTableId;

    public OrderTableChangeOrderCloseEvent(Object source, Long orderTableId) {
        super(source);
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
