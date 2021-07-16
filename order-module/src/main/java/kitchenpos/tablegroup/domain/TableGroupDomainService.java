package kitchenpos.tablegroup.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class TableGroupDomainService {
    public void addGroup(TableGroup tableGroup, List<OrderTable> orderTables) {
        validateGroupAvailable(orderTables);
        orderTables.forEach(orderTable -> orderTable.group(tableGroup));
    }

    public TableGroup group(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        addGroup(tableGroup, orderTables);
        return tableGroup;
    }

    public void ungroup(List<OrderTable> orderTables, List<Order> orders) {
        validateUngroup(orders);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateGroupAvailable(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < TableGroup.TABLE_COUNT_MIN) {
            throw new IllegalArgumentException("주문테이블이 " + TableGroup.TABLE_COUNT_MIN + "개 미만입니다.");
        }

        if (orderTables.stream()
                .filter(orderTable -> !orderTable.isEmpty())
                .findFirst()
                .isPresent()) {
            throw new IllegalArgumentException("주문테이블은 빈테이블이어야 합니다.");
        }
    }

    private void validateUngroup(List<Order> orders) {
        if (orders.stream()
                .anyMatch(order -> !order.isCompletion())) {
            throw new IllegalArgumentException("주문테이블의 주문상태가 조리나 식사입니다.");
        }
    }
}
