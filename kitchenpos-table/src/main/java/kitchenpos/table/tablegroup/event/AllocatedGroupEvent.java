package kitchenpos.table.tablegroup.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class AllocatedGroupEvent extends ApplicationEvent {

    private static final long serialVersionUID = 5998927563816950725L;
    private final List<Long> orderTableIds;
    private final long tableGroupId;

    public AllocatedGroupEvent(Object source, List<Long> orderTableIds, Long tableGroupId) {
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
