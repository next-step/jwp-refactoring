package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {
    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> toIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public boolean hasSize(int size) {
        return orderTables.size() == size;
    }

    public boolean hasAnyNotEmpty() {
        return orderTables.stream()
            .anyMatch(OrderTable::isNotEmpty);
    }

    public boolean hasAnyTableGroupRegistered() {
        return orderTables.stream()
            .anyMatch(OrderTable::isTableGroupRegistered);
    }

    public void group(Long tableGroupId) {
        orderTables.forEach(orderTable -> orderTable.group(tableGroupId));
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> toList() {
        return orderTables;
    }
}
