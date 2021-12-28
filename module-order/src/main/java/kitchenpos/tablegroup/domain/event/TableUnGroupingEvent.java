package kitchenpos.tablegroup.domain.event;

import org.springframework.context.ApplicationEvent;

public class TableUnGroupingEvent extends ApplicationEvent {

    private final Long tableGroupId;

    public TableUnGroupingEvent(Long tableGroupId) {
        super(tableGroupId);
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    @Override
    public String toString() {
        return "TableUnGroupingEvent{" +
            "tableGroupId=" + tableGroupId +
            ", source=" + source +
            '}';
    }
}
