package kitchenpos.table.domain;

import java.util.List;

public class TableUngroupEvent {
    private final Long tableGroupId;


    private TableUngroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public static TableUngroupEvent of(Long tableGroupId) {
        return new TableUngroupEvent(tableGroupId);
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
