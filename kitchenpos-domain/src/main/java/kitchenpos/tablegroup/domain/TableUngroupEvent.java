package kitchenpos.tablegroup.domain;

/**
 * packageName : kitchenpos.tablegroup.domain
 * fileName : TableUngroupEvent
 * author : haedoang
 * date : 2021-12-27
 * description :
 */
public class TableUngroupEvent {
    private final Long tableGroupId;

    public TableUngroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
