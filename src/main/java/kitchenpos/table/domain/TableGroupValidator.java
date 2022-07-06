package kitchenpos.table.domain;

import kitchenpos.table.dto.OrderTableIdRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupValidator {
    public TableGroupValidator() {
    }

    public void validate(List<OrderTableIdRequest> orderTableRequests, OrderTables orderTables) {
        if (orderTableRequests== null || orderTableRequests.size() < 2) {
            throw new IllegalArgumentException();
        }

        if (orderTableRequests.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        if (orderTables.existsEmptyTable()) {
            throw new IllegalArgumentException();
        }

        if (orderTables.existsTableGroup()) {
            throw new IllegalArgumentException();
        }
    }
}
