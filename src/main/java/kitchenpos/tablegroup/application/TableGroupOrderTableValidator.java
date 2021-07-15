package kitchenpos.tablegroup.application;

import java.util.List;

public interface TableGroupOrderTableValidator {
    void validateOrderTablesConditionForCreatingTableGroup(List<Long> orderTableIds);
}
