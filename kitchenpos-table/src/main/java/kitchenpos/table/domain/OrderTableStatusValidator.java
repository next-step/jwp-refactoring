package kitchenpos.table.domain;

import java.util.List;

public interface OrderTableStatusValidator {
    void validateOrderStatus(Long orderTableId);

    void validateOrderTablesStatus(List<OrderTable> orderTables);
}
