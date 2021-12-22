package kitchenpos.order.domain;

import kitchenpos.table.domain.TableStatus;
import org.springframework.context.ApplicationEvent;

public class OrderStatusEvent extends ApplicationEvent {
    private Long orderTableId;
    private Long orderId;
    private OrderStatus orderStatus;

    public OrderStatusEvent(Object source, Order order) {
        super(source);
        this.orderTableId = order.getOrderTableId();
        this.orderId = order.getId();
        this.orderStatus = order.getOrderStatus();
    }

    public static OrderStatusEvent of(Object source, Order order) {
        return new OrderStatusEvent(source, order);
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public TableStatus getStatus() {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            return TableStatus.COMPLETION;
        }
        return TableStatus.ORDERED;
    }

    public Long getOrderId() {
        return this.orderId;
    }
}
