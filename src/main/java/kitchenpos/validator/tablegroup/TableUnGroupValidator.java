package kitchenpos.validator.tablegroup;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;

public interface TableUnGroupValidator {

    void validate(List<OrderTable> orderTables);
}
