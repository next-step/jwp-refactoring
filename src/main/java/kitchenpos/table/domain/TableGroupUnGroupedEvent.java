package kitchenpos.table.domain;

import java.util.List;

public class TableGroupUnGroupedEvent {
    private List<Long> orderTableIds;

    public TableGroupUnGroupedEvent(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
