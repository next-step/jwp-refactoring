package kichenpos.order.order.domain.event;

import kichenpos.order.order.domain.Order;
import org.springframework.util.Assert;

public class OrderStatusChangedEvent {

    private final Order order;

    private OrderStatusChangedEvent(Order order) {
        Assert.notNull(order, "이벤트 대상 주문은 필수입니다.");
        this.order = order;
    }

    public static OrderStatusChangedEvent from(Order order) {
        return new OrderStatusChangedEvent(order);
    }

    public long tableId() {
        return order.tableId();
    }

    public boolean isCompleted() {
        return order.isCompleted();
    }
}
