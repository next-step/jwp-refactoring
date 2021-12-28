package kitchenpos.tableGroup.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidation {

    public void valid(List<OrderTable> savedOrderTables, List<OrderTableRequest> orderTableRequests) {
        if (savedOrderTables.size() != orderTableRequests.size()) {
            throw new IllegalArgumentException();
        }
    }
}
