package kichenpos.order.order.domain.event;

import kichenpos.order.order.domain.Order;
import org.springframework.util.Assert;

public class OrderCreatedEvent {

    private final Order order;

    private OrderCreatedEvent(Order order) {
        Assert.notNull(order, "이벤트 대상 주문은 필수입니다.");
        this.order = order;
    }

    public static OrderCreatedEvent from(Order order) {
        return new OrderCreatedEvent(order);
    }

    public long tableId() {
        return order.tableId();
    }
}
