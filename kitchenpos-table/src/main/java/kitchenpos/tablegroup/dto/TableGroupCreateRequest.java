package kitchenpos.tablegroup.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private final List<TableRequest> tableRequests;

    protected TableGroupCreateRequest() {
        tableRequests = new ArrayList<>();
    }

    public TableGroupCreateRequest(List<TableRequest> tableRequests) {
        this.tableRequests = tableRequests;
    }

    public List<TableRequest> getTableRequests() {
        return tableRequests;
    }

    public List<Long> getTableIds() {
        return this.tableRequests.stream()
                .map(TableRequest::getId)
                .collect(Collectors.toList());
    }
}
