package kitchenpos.table.validator;

import kitchenpos.table.domain.OrderTable;

public interface TableValidator {
    void isPossibleChangeEmpty(OrderTable orderTable);
}
