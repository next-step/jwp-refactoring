package kitchenpos.domain.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;

public interface SafeOrderTableInTableGroup {
    OrderTable getOrderTable(Long orderTableId);
}
