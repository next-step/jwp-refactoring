package kitchenpos.order.event;

import java.util.List;

public class OrderValidEvent {

    private List<Long> menuIds;
    private Long orderTableId;

    public OrderValidEvent(List<Long> menuIds, Long orderTableId) {
        this.menuIds = menuIds;
        this.orderTableId = orderTableId;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
