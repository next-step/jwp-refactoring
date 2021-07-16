package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class TableGroupValidator {
    public static void validOrderTablesSize(List<OrderTableRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("그룹테이블은 2개 이상이어야 그룹화가 가능합니다.");
        }
    }

    public static void validGroupOrderTableList(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.validGroupOrderTable();
        }
    }

    public static void validRegisteredOrderTable(int orderTableSize, int orderTableIdSize) {
        if (orderTableSize != orderTableIdSize) {
            throw new IllegalArgumentException("등록된 주문테이블만 그룹화 할 수 있습니다.");
        }
    }
}
