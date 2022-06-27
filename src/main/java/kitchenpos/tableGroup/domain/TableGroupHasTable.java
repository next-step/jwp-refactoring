package kitchenpos.tableGroup.domain;

import java.util.List;

public class TableGroupHasTable {

    private Long tableGroupId;
    private List<Long> orderTableIds;

    public TableGroupHasTable(Long tableGroupId, List<Long> orderTableIds) {
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }
}
