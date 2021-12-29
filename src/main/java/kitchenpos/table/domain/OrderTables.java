package kitchenpos.table.domain;

import java.util.List;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.tablegroup.domain.GroupingTableEvent;

public class OrderTables {
    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        if (orderTables.isEmpty()) {
            throw new KitchenposNotFoundException();
        }
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void unGroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public void group(GroupingTableEvent event) {
        checkSameSize(event.getTablesSize());
        checkNotContainsUsedTable();
        group(event.getTableGroupId());
    }

    private void checkNotContainsUsedTable() {
        if (orderTables.stream()
            .anyMatch(OrderTable::cannotBeGrouped)) {
            throw new KitchenposException(KitchenposErrorCode.CONTAINS_USED_TABLE);
        }
    }

    private void checkSameSize(int size) {
        if (size != orderTables.size()) {
            throw new KitchenposException(KitchenposErrorCode.INVALID_TABLE_SIZE);
        }
    }

    private void group(Long tableGroupId) {
        for (OrderTable orderTable : orderTables) {
            orderTable.group(tableGroupId);
        }
    }
}
