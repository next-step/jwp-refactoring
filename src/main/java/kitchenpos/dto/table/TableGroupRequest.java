package kitchenpos.dto.table;

import static java.util.stream.Collectors.*;

import java.util.List;

public class TableGroupRequest {
    private List<TableRequest> tableRequests;

    public TableGroupRequest() {}

    public TableGroupRequest(List<TableRequest> tableRequests) {
        this.tableRequests = tableRequests;
    }

    public List<TableRequest> getTableRequests() {
        return tableRequests;
    }

    public List<Long> getTableIds() {
        return tableRequests.stream().map(TableRequest::getTableId).collect(toList());
    }
}
