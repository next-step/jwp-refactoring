package kitchenpos.domain.tablegroup.event;

import java.util.List;
import kitchenpos.domain.tablegroup.TableGroup;

public class TableGroupingEvent {

    private TableGroup tableGroup;
    private List<Long> orderTableIds;

    public TableGroupingEvent(TableGroup tableGroup, List<Long> orderTableIds) {
        this.tableGroup = tableGroup;
        this.orderTableIds = orderTableIds;
    }

    public TableGroup getTableGroup() {
        return this.tableGroup;
    }

    public List<Long> getOrderTableIds() {
        return this.orderTableIds;
    }
}
