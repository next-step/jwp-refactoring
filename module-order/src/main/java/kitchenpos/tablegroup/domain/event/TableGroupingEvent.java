package kitchenpos.tablegroup.domain.event;

import java.util.List;
import org.springframework.context.ApplicationEvent;

public class TableGroupingEvent extends ApplicationEvent {

    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    public TableGroupingEvent(Long tableGroupId, List<Long> orderTableIds) {
        super(tableGroupId);
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    @Override
    public String toString() {
        return "TableGroupingEvent{" +
            "tableGroupId=" + tableGroupId +
            ", orderTableIds=" + orderTableIds +
            '}';
    }
}
