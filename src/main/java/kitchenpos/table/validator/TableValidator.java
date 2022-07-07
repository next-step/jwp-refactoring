package kitchenpos.table.validator;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public interface TableValidator {

    void validateOrderTableAndOrderStatus(final Long orderTableId);

    void validateOrderTablesExist(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables);

    void validateOrderTableStatus(TableGroup tableGroup);
}
