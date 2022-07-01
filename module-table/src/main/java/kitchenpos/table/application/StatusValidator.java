package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTables;

import java.util.List;

public interface StatusValidator {
    void validateOrderTableNotCompletion(Long orderTableId);

    void validateOrderTablesNotCompletion(OrderTables orderTables);

    boolean isExistsOrderTablesAndNotCompletion(List<Long> orderTableIds);
}
