package table_group.application;

import java.util.List;
import table.domain.OrderTable;

public interface TableGroupValidator {

    void checkValidUngroup(List<Long> orderTableIds);

    void checkCreatable(List<OrderTable> orderTables, List<Long> requestOrderTableIds);
}
