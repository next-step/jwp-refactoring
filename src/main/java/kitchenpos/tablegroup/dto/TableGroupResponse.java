package kitchenpos.tablegroup.dto;

import kitchenpos.table.dto.TableResponse;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {
    private long id;
    private LocalDateTime createdDate;
    private List<TableResponse> tableResponses;

    protected TableGroupResponse(){}

    public TableGroupResponse(long id, LocalDateTime createdDate, List<TableResponse> tableResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.tableResponses = tableResponses;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<TableResponse> getTableResponses() {
        return tableResponses;
    }
}
