package kitchenpos.common.event;

import java.util.List;

public class UngroupedTablesEvent {
    private List<Long> orderTables;

    public UngroupedTablesEvent(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
