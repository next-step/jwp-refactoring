package kitchenpos.order.domain;

import org.springframework.context.ApplicationEvent;

public class OrderCompletionEvent extends
    ApplicationEvent {

    private final Order order;
    private final Long tableId;

    public OrderCompletionEvent(Order order) {
        super(order);
        this.order = order;
        this.tableId = order.getOrderTable().getId();
    }

    public Order getOrder() {
        return order;
    }

    public Long getTableId() {
        return tableId;
    }
}
