package kitchenpos.order.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public interface OrderTableService {

    void validateOrderTablesStatus(List<OrderTable> orderTables);

    void validateOrderTableStatus(OrderTable orderTable);
}
