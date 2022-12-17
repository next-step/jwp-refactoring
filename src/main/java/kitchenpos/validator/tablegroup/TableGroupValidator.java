package kitchenpos.validator.tablegroup;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;

public abstract class TableGroupValidator {

    protected void validate(OrderTable orderTable) {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다");
    }

    protected void validate(List<OrderTable> orderTables) {
        throw new UnsupportedOperationException("지원하지 않는 기능입니다");
    }
}
