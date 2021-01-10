package kitchenpos.domain.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;

public interface SafeOrderTable {
    OrderTable getOrderTable(Long orderTableId);
}
