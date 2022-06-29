package kitchenpos.tablegroup.domain;

import java.util.Collections;
import java.util.List;

public class TableGroupUngroupedEvent {

    private final List<Long> orderTableIds;

    public TableGroupUngroupedEvent(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return Collections.unmodifiableList(orderTableIds);
    }
}
