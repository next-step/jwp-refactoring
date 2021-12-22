package kitchenpos.table.domain;

import org.springframework.context.ApplicationEvent;

public abstract class OrderTableStatusEvent extends ApplicationEvent {

    protected Long orderTableId;
    protected Long orderId;
    protected TableStatus tableStatus;

    protected OrderTableStatusEvent(Object source, Long orderTableId, Long orderId,
        TableStatus tableStatus) {
        super(source);
        this.orderTableId = orderTableId;
        this.orderId = orderId;
        this.tableStatus = tableStatus;
    }

    public abstract Long getOrderTableId();

    public abstract TableStatus getTableStatus();

    public abstract Long getOrderId();

}
