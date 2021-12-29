package kitchenpos.tablegroup.domain;

import java.util.List;

/**
 * packageName : kitchenpos.tablegroup.domain
 * fileName : TableGroupEvent
 * author : haedoang
 * date : 2021-12-27
 * description : 그룹 이벤트
 */
public class TableGroupEvent {
    private final List<Long> tableIds;
    private final Long groupTableId;

    public TableGroupEvent(List<Long> tableIds, Long groupTableId) {
        this.tableIds = tableIds;
        this.groupTableId = groupTableId;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }

    public Long getGroupTableId() {
        return groupTableId;
    }
}
