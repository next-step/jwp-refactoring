package kitchenpos.tablegroup.event;

import kitchenpos.table.event.DeAllocatedGroupEvent;

public class DeAllocatedGroupEventImpl extends DeAllocatedGroupEvent {

    private static final long serialVersionUID = 350968960613003553L;
    private final long tableGroupId;

    public DeAllocatedGroupEventImpl(Object source, long tableGroupId) {
        super(source);
        this.tableGroupId = tableGroupId;
    }

    public long getTableGroupId() {
        return tableGroupId;
    }
}
