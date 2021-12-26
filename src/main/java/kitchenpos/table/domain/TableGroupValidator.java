package kitchenpos.table.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {
    private final OrderStatusChecker orderStatusChecker;

    public TableGroupValidator(final OrderStatusChecker orderStatusChecker) {
        this.orderStatusChecker = orderStatusChecker;
    }

    public void validateToGroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateOrderTable(orderTable);
        }
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("아직 단체 지정이 되지 않은 빈 테이블만 단체 지정이 가능합니다");
        }
    }

    public void validateToUngroup(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderStatusChecker.existsNotCompletedOrderByOrderTableIds(orderTableIds)) {
            throw new IllegalArgumentException("주문 상태가 '조리' 혹은 '식사'일 경우, 단체 지정을 해제할 수 없습니다");
        }
    }
}
