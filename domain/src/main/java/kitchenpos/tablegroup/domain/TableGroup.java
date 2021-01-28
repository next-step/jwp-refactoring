package kitchenpos.tablegroup.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class TableGroup extends BaseEntity {
    public TableGroup(List<OrderTable> orderTables) {
        validate(orderTables);
    }

    protected TableGroup() {
    }

    private static void validate(List<OrderTable> orderTables) {
        if (isNotValidOrderTables(orderTables)) {
            throw new IllegalArgumentException("주문 그룹으로 설정될 주문 테이블은 Group으로 설정되지 않거나, 점유되어있지 않아야합니다.");
        }
    }

    private static boolean isNotValidOrderTables(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> orderTable.isNotEmpty() || orderTable.hasTableGroup());
    }
}
