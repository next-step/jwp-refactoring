package kitchenpos.tablegroup.event;

import kitchenpos.table.event.AllocatedGroupEvent;

import java.util.List;

public class AllocatedGroupEventImpl extends AllocatedGroupEvent {

    private static final long serialVersionUID = 5998927563816950725L;
    private final List<Long> orderTableIds;
    private final long tableGroupId;

    public AllocatedGroupEventImpl(Object source, List<Long> orderTableIds, Long tableGroupId) {
        super(source);
        this.orderTableIds = orderTableIds;
        this.tableGroupId = tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public long getTableGroupId() {
        return tableGroupId;
    }
}
