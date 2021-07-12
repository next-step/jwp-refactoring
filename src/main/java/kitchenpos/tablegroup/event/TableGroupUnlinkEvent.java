package kitchenpos.tablegroup.event;

import org.springframework.context.ApplicationEvent;

public class TableGroupUnlinkEvent {
    private Long tableGroupId;

    public TableGroupUnlinkEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
