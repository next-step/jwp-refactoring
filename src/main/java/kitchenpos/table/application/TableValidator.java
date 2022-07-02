package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public interface TableValidator {

    void checkValidChangeEmpty(OrderTable orderTable);

    void checkValidUngroup(List<Long> orderTableIds);
}
