package kitchenpos.table.dto;

import kitchenpos.table.domain.tablegroup.TableGroup;

import java.time.LocalDateTime;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;

    public TableGroupResponse() {
    }

    private TableGroupResponse(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.createdDate = tableGroup.getCreatedDate();
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
