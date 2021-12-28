package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableGroupRequest {

    @Size(min = 2)
    private List<OrderTableRequest> orderTables = new ArrayList<>();

    public TableGroupRequest() {

    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroup toEntity() {
        return TableGroup.setUp();
    }

    public List<Long> toOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(toList());
    }
}
