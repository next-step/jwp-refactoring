package kitchenpos.order.event;

import java.util.List;

public class OrderCreatedEvent {

    private final Long orderTableId;
    private final List<Long> menuIds;

    public OrderCreatedEvent(Long orderTableId, List<Long> menuIds) {
        this.orderTableId = orderTableId;
        this.menuIds = menuIds;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }
}
