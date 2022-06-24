package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private List<IdOfOrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<IdOfOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
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
