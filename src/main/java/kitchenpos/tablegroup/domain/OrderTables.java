package kitchenpos.tablegroup.domain;

import java.util.List;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.table.domain.OrderTable;

public class OrderTables {
    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        if (orderTables.isEmpty()) {
            throw new KitchenposNotFoundException();
        }
        this.orderTables = orderTables;
    }

    public void checkSameSize(int size) {
        if (size != orderTables.size()) {
            throw new KitchenposException(KitchenposErrorCode.INVALID_TABLE_SIZE);
        }
    }

    public void checkNotContainsUsedTable() {
        if (orderTables.stream()
            .anyMatch(OrderTable::cannotBeGrouped)) {
            throw new KitchenposException(KitchenposErrorCode.CONTAINS_USED_TABLE);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void unGroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.referenceTableGroupId(null);
        }
    }

    public void referenceGroupId(Long tableGroupId) {
        for (OrderTable orderTable : orderTables) {
            orderTable.updateEmpty(false);
            orderTable.referenceTableGroupId(tableGroupId);
        }
    }
}
