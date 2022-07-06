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
            throw new IllegalArgumentException("단체 테이블은 주문테이블이 2개 이상이어야 합니다.");
        }

        if (orderTableRequests.size() != orderTables.size()) {
            throw new IllegalArgumentException("요청된 주문 테이블과 등록된 주문 테이블이 일치하지 않습니다.");
        }

        if (orderTables.existsEmptyTable()) {
            throw new IllegalArgumentException("비어있는 주문 테이블이 존재합니다.");
        }

        if (orderTables.existsTableGroup()) {
            throw new IllegalArgumentException("단체 테이블 존재합니다.");
        }
    }
}
