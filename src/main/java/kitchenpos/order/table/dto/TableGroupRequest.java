package kitchenpos.order.table.dto;

import kitchenpos.order.table.domain.OrderTable;
import kitchenpos.order.table.domain.TableGroup;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return TableGroup.create();
    }

    public List<Long> toOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(toList());
    }
}
