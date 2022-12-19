package kitchenpos.tablegroup.validator;

import kitchenpos.ordertable.domain.OrderTable;

public interface TableGroupValidator {

    void validate(OrderTable orderTable);

}
