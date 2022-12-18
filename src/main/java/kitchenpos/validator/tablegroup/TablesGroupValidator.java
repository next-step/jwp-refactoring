package kitchenpos.validator.tablegroup;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;

public interface TablesGroupValidator {

    void validate(List<OrderTable> orderTables);
}
