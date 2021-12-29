package kitchenpos.ordertable.event;

import org.springframework.context.ApplicationEvent;

public class TableChangeOrderCloseEvent extends ApplicationEvent {

    private Long orderTableId;

    public TableChangeOrderCloseEvent(Object source, Long orderTableId) {
        super(source);
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
