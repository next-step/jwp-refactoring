package kitchenpos.order.domain;

import kitchenpos.table.domain.TableStatus;
import org.springframework.context.ApplicationEvent;

public class OrderStatusEvent extends ApplicationEvent {
    private Long orderTableId;
    private OrderStatus orderStatus;

    public OrderStatusEvent(Object source, Long orderTableId, OrderStatus orderStatus) {
        super(source);
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public static OrderStatusEvent of(Object source, Long orderTableId, OrderStatus orderStatus) {
        return new OrderStatusEvent(source, orderTableId, orderStatus);
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public TableStatus getStatus() {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            return TableStatus.EMPTY;
        }
        return TableStatus.ORDERED;
    }
}
