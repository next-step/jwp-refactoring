package kitchenpos.table;

import org.springframework.util.CollectionUtils;
import kitchenpos.table.exception.InvalidOrderTablesException;
import kitchenpos.tablegroup.TableGroup;

import java.util.List;

public class OrderTables {
    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        validate();
    }

    private void validate() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidOrderTablesException("주문 테이블은 2개 이상이어야 합니다.");
        }
    }

    public void group(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.group(tableGroup));
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
