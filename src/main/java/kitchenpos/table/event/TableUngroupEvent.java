package kitchenpos.table.event;

import java.util.List;

public class TableUngroupEvent {
    private final List<Long> orderTableIds;

    public TableUngroupEvent(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
