package kitchenpos.domain.table.domain;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {

    private static final int MIN_REQUEST_ORDER_TABLE_SIZE = 2;

    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void group(Long tableGroupId) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(tableGroupId);
        }
    }

    public void checkOrderTables(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_REQUEST_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException();
        }

        if (!isSameSize(orderTableIds)) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.checkNonEmptyInGroup();
        }
    }

    private boolean isSameSize(List<Long> orderTableIds) {
        return orderTables.size() == orderTableIds.size();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void ungroup() {
        orderTables.forEach(orderTable -> orderTable.ungroup());
    }
}
