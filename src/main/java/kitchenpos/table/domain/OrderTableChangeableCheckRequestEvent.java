package kitchenpos.table.domain;

import org.springframework.context.ApplicationEvent;

public class OrderTableChangeableCheckRequestEvent extends ApplicationEvent {

    private final Long orderTableId;

    public OrderTableChangeableCheckRequestEvent(Long orderTableId) {
        super(orderTableId);
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
