package kitchenpos.table.tablegroup.event;

import org.springframework.context.ApplicationEvent;

public class DeAllocatedGroupEvent extends ApplicationEvent {

    private static final long serialVersionUID = 350968960613003553L;
    private final long tableGroupId;

    public DeAllocatedGroupEvent(Object source, long tableGroupId) {
        super(source);
        this.tableGroupId = tableGroupId;
    }

    public long getTableGroupId() {
        return tableGroupId;
    }
}
