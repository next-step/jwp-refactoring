package kitchenpos.domain.table;

import org.springframework.context.ApplicationEvent;

public class OrderTableChangeEmptyEvent extends ApplicationEvent {

    private final Long orderTableId;

    public OrderTableChangeEmptyEvent(Long orderTableId) {
        super(orderTableId);
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
