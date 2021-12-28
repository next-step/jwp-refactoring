package kitchenpos.table.application;

import org.springframework.context.ApplicationEvent;

public class TableUnGroupingEvent extends ApplicationEvent {

    private final Long tableGroupId;

    public TableUnGroupingEvent(Object source, Long tableGroupId) {
        super(source);
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
