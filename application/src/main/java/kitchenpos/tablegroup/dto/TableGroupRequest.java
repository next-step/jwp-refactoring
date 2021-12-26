package kitchenpos.tablegroup.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public boolean isEmptyOrderTables() {
        return CollectionUtils.isEmpty(orderTables);
    }

    public int getOrderTablesSize() {
        return orderTables.size();
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
