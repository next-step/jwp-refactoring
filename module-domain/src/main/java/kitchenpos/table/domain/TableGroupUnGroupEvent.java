package kitchenpos.table.domain;

import org.springframework.context.ApplicationEvent;

public class TableGroupUnGroupEvent extends ApplicationEvent {

    private final Long tableGroupId;


    public TableGroupUnGroupEvent(Long tableGroupId) {
        super(tableGroupId);
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
