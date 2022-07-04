package kitchenpos.table_group.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public interface TableGroupValidator {

    void checkValidUngroup(List<Long> orderTableIds);

    void checkCreatable(List<OrderTable> orderTables, List<Long> requestOrderTableIds);
}
