package kitchenpos.support;

import kitchenpos.table.domain.OrderTable;

import java.util.Collection;

public interface OrderSupport {
    boolean isUsingTable(OrderTable table);

    boolean isUsingTables(Collection<OrderTable> orderTables);
}
