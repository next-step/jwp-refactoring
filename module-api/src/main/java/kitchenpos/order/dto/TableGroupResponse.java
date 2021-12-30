package kitchenpos.order.dto;

import kitchenpos.order.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<Long> tableIds;

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<Long> tableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.tableIds = tableIds;
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<Long> tableIds) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), tableIds);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
