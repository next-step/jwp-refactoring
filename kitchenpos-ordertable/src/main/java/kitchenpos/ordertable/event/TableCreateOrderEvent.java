package kitchenpos.ordertable.event;

import org.springframework.context.ApplicationEvent;

public class TableCreateOrderEvent extends ApplicationEvent {

    private Long orderTableId;

    public TableCreateOrderEvent(Object source, Long orderTableId) {
        super(source);
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
