package kitchenpos.tablegroup.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dto.IdOfOrderTableRequest;

public class TableGroupRequest {

    private List<IdOfOrderTableRequest> orderTables;

    protected TableGroupRequest() {
    }

    protected TableGroupRequest(List<IdOfOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupRequest from(List<Long> ids) {
        List<IdOfOrderTableRequest> idOfOrderTableRequests = ids.stream().
                map(IdOfOrderTableRequest::new).
                collect(Collectors.toList());
        return new TableGroupRequest(idOfOrderTableRequests);
    }

    public List<Long> toOrderTableIds() {
        return orderTables.stream()
                .map(IdOfOrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public List<IdOfOrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
