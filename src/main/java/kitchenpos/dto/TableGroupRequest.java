package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import kitchenpos.domain.OrderTable;

public class TableGroupRequest {
    private List<OrderTable> orderTables;

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> ids() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public boolean isEmptyOrderTables() {
        return CollectionUtils.isEmpty(orderTables);
    }

    public int orderTablesSize() {
        if (isEmptyOrderTables()) {
            return 0;
        }

        return orderTables.size();
    }
}
