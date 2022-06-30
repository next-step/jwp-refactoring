package kitchenpos.dto.event;

import java.util.List;

public class TableUngroupedEvent {

    private final List<Long> orderTableIds;

    public TableUngroupedEvent(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
