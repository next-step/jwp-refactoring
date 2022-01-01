package kitchenpos.common.event.orderCompletion;

import org.springframework.context.ApplicationEvent;

public class OrderCompletionEvent extends ApplicationEvent {

    private final Long tableId;

    public OrderCompletionEvent(Long tableId) {
        super(tableId);
        this.tableId = tableId;
    }

    public Long getTableId() {
        return tableId;
    }
}
