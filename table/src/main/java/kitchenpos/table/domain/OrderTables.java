package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {
    private final List<OrderTable> orderTables;

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<Long> toIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public boolean hasSize(int size) {
        return orderTables.size() == size;
    }

    public void group(Long tableGroupId) {
        validateHasAnyNotEmpty();
        validateHasAnyTableGroupRegistered();

        orderTables.forEach(orderTable -> orderTable.group(tableGroupId));
    }

    private void validateHasAnyNotEmpty() {
        if (hasAnyNotEmpty()) {
            throw new IllegalStateException("모든 주문 테이블은 빈 테이블 상태이어야 합니다.");
        }
    }

    private void validateHasAnyTableGroupRegistered() {
        if (hasAnyTableGroupRegistered()) {
            throw new IllegalStateException("이미 단체 지정되어있는 테이블이 존재합니다.");
        }
    }

    private boolean hasAnyNotEmpty() {
        return orderTables.stream()
            .anyMatch(OrderTable::isNotEmpty);
    }

    private boolean hasAnyTableGroupRegistered() {
        return orderTables.stream()
            .anyMatch(OrderTable::isTableGroupRegistered);
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> toList() {
        return orderTables;
    }
}
