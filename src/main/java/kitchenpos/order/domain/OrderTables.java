package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {
    private static final int MIN_ORDER_TABLE_SIZE = 2;
    private static final String ERROR_MESSAGE_INVALID_TABLE = "빈 테이블이 아니거나 단체 지정된 테이블입니다.";
    private static final String ERROR_MESSAGE_TABLE_COUNT = "단체지정하려면 테이블 수가 2개 이상이어야 합니다.";

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables, TableGroup tableGroup) {
        validateTablesEmpty(orderTables);
        validateOrderTableSize(orderTables);
        setTableGroup(orderTables, tableGroup);
        this.orderTables = orderTables;
    }

    private void validateTablesEmpty(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.isGrouped()) {
                throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_TABLE);
            }
        }
    }

    private void validateOrderTableSize(List<OrderTable> items) {
        if (items.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException(ERROR_MESSAGE_TABLE_COUNT);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    private void setTableGroup(List<OrderTable> items, TableGroup tableGroup) {
        items.forEach(it -> it.setTableGroup(tableGroup));
    }

    public void unGroup() {
        orderTables.forEach(OrderTable::unGroup);
    }
}
